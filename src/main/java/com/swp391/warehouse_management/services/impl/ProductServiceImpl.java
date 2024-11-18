package com.swp391.warehouse_management.services.impl;

import com.swp391.warehouse_management.common.enums.OrderStatus;
import com.swp391.warehouse_management.dto.ProductExpiredStatusDTO;
import com.swp391.warehouse_management.dto.ProductQuantityInStockTODO;
import com.swp391.warehouse_management.entities.DestroyProductHistory;
import com.swp391.warehouse_management.entities.ExportOrder;
import com.swp391.warehouse_management.entities.ExportOrderDetails;
import com.swp391.warehouse_management.entities.Product;
import com.swp391.warehouse_management.entities.ProductInfo;
import com.swp391.warehouse_management.entities.Stock;
import com.swp391.warehouse_management.entities.StockPosition;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.repositories.DestroyProductHistoryRepository;
import com.swp391.warehouse_management.repositories.ExportOrderDetailsRepository;
import com.swp391.warehouse_management.repositories.ExportOrderRepository;
import com.swp391.warehouse_management.repositories.ProductInfoRepository;
import com.swp391.warehouse_management.repositories.ProductRepository;
import com.swp391.warehouse_management.repositories.StockPositionRepository;
import com.swp391.warehouse_management.repositories.StockRepository;
import com.swp391.warehouse_management.services.AuthService;
import com.swp391.warehouse_management.services.ProductService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = { Exception.class, Error.class })
public class ProductServiceImpl implements ProductService {

  private final AuthService authService;
  private final ProductRepository productRepository;
  private final StockPositionRepository stockPositionRepository;
  private final StockRepository stockRepository;
  private final ExportOrderRepository exportOrderRepository;
  private final ExportOrderDetailsRepository exportOrderDetailsRepository;
  private final ProductInfoRepository productInfoRepository;
  private final DestroyProductHistoryRepository destroyProductHistoryRepository;

  @Override
  public List<Product> getUnexpiredProductsByStockId(String stockId) {
    return productRepository.findByStockIdAndExpiredDateAfter(stockId, LocalDate.now());
  }

  @Override
  public int getNumberOfProductsByPositionId(String stockId) {
    return productRepository.countByPositionId(stockId);
  }

  public void changePositionInternalByProductId(
    String productId,
    List<String> positions,
    List<Integer> quantities
  ) {
    Product product = productRepository
      .findById(productId)
      .orElseThrow(() -> new RuntimeException("Product not found!"));

    Map<String, Integer> positionQuantityMap = IntStream.range(0, positions.size())
      .boxed()
      .collect(Collectors.toMap(positions::get, quantities::get));

    positionQuantityMap.forEach((positionId, quantity) -> {
      Optional<Product> existedProductAtStockPosition =
        productRepository.findByProductInfoIdAndStockPositionIdAndExpiredDate(
          product.getProductInfo().getId(),
          positionId,
          product.getExpiredDate()
        );

      if (existedProductAtStockPosition.isPresent()) {
        Product existedProduct = existedProductAtStockPosition.get();

        existedProduct.setQuantity(existedProduct.getQuantity() + quantity);
        productRepository.save(existedProduct);
      } else {
        StockPosition stockPosition = stockPositionRepository
          .findById(positionId)
          .orElseThrow(() -> new RuntimeException("Stock position not found!"));

        productRepository.save(
          Product.builder()
            .productInfo(product.getProductInfo())
            .expiredDate(product.getExpiredDate())
            .stockPosition(stockPosition)
            .quantity(quantity)
            .build()
        );
      }

      product.setQuantity(product.getQuantity() - quantity);
    });

    if (product.getQuantity() == 0) {
      productRepository.delete(product);
      return;
    }

    productRepository.save(product);
  }

  @Override
  public void moveProductsToAnotherStock(
    String destinationStockId,
    List<String> productIds,
    List<Integer> quantities
  ) {
    User currentUser = authService.getCurrentUser().get();
    Stock destinationStock = stockRepository
      .findById(destinationStockId)
      .orElseThrow(() -> new RuntimeException("Destination stock not found!"));

    Map<Product, Integer> productQuantityMap = IntStream.range(0, productIds.size())
      .boxed()
      .collect(
        Collectors.toMap(
          i ->
            productRepository
              .findById(productIds.get(i))
              .orElseThrow(() -> new RuntimeException("Product not found!")),
          quantities::get
        )
      );

    productQuantityMap.forEach((product, quantity) -> {
      product.setQuantity(product.getQuantity() - quantity);
      productRepository.save(product);

      if (product.getQuantity() == 0) {
        productRepository.delete(product);
      }
    });

    Map<AbstractMap.SimpleEntry<ProductInfo, LocalDate>, Integer> productInfoQuantityMap =
      productQuantityMap
        .keySet()
        .stream()
        .collect(
          Collectors.toMap(
            product ->
              new AbstractMap.SimpleEntry<>(product.getProductInfo(), product.getExpiredDate()),
            product -> productQuantityMap.get(product),
            (quantity1, quantity2) -> quantity1 + quantity2
          )
        );

    ExportOrder exportOrder = ExportOrder.builder()
      .exportUser(currentUser)
      .sourceStock(currentUser.getStock())
      .destinationStock(destinationStock)
      .status(OrderStatus.PENDING)
      .build();
    exportOrderRepository.save(exportOrder);

    productInfoQuantityMap.forEach((entry, quantity) -> {
      exportOrderDetailsRepository.save(
        ExportOrderDetails.builder()
          .exportOrder(exportOrder)
          .productInfo(entry.getKey())
          .expiredDate(entry.getValue())
          .quantity(quantity)
          .build()
      );
    });
  }

  @Override
  public List<Product> getInvoiceListByStockId(
    String stockId,
    List<String> productInfoIdList,
    List<Integer> quantities
  ) {
    List<Product> invoiceList = new ArrayList<>();
    for (int i = 0; i < productInfoIdList.size(); i++) {
      List<Product> unexpiredProductsInStock =
        productRepository.findbyStockIdAndProductInfoIdAndExpiredDateAfter(
          stockId,
          productInfoIdList.get(i),
          LocalDate.now()
        );
      addProductsNearlyExpiredByQuantityToInvoiceList(
        unexpiredProductsInStock,
        quantities.get(i),
        invoiceList
      );
    }
    return invoiceList;
  }

  private List<Product> addProductsNearlyExpiredByQuantityToInvoiceList(
    List<Product> products,
    int quantity,
    List<Product> invoiceList
  ) {
    int quantityLeft = quantity;
    int index = 0;
    Set<StockPosition> PositionTaken = new HashSet<>();
    while (index < products.size() && quantityLeft > 0) {
      LocalDate currExpiredDate = products.get(index).getExpiredDate();
      int totalQuantity = totalQuantityByExpiredDate(index, products, currExpiredDate);
      if (totalQuantity <= quantityLeft) {
        index = addAllProductsWithSameExpiryDateToInvoiceList(
          products,
          PositionTaken,
          currExpiredDate,
          index,
          invoiceList
        );
        quantityLeft -= totalQuantity;
      } else {
        Set<Product> productTaken = new HashSet<>();
        if (!PositionTaken.isEmpty()) {
          quantityLeft = addProductsFromPreviousPositionWithSameExpiryDateToInvoiceList(
            products,
            PositionTaken,
            productTaken,
            currExpiredDate,
            quantityLeft,
            index,
            invoiceList
          );
        }
        if (quantityLeft > 0) {
          quantityLeft = addRemainingProductsWithSameExpiryDateToInvoiceList(
            products,
            productTaken,
            currExpiredDate,
            quantityLeft,
            index,
            invoiceList
          );
        }
      }
    }
    if (quantityLeft != 0) {
      throw new RuntimeException("Current quantity in stock is not enough!");
    }
    return invoiceList;
  }

  private int totalQuantityByExpiredDate(int index, List<Product> products, LocalDate expiredDate) {
    int totalQuantity = 0;
    for (int i = index; i < products.size(); i++) {
      if (products.get(i).getExpiredDate().equals(expiredDate)) {
        totalQuantity += products.get(i).getQuantity();
      } else break;
    }
    return totalQuantity;
  }

  private int addAllProductsWithSameExpiryDateToInvoiceList(
    List<Product> products,
    Set<StockPosition> PositionTaken,
    LocalDate expiryDate,
    int startIndex,
    List<Product> invoiceList
  ) {
    int index = startIndex;
    while (index < products.size() && expiryDate.equals(products.get(index).getExpiredDate())) {
      PositionTaken.add(products.get(index).getStockPosition());
      invoiceList.add(products.get(index));
      index++;
    }
    return index;
  }

  private int addProductsFromPreviousPositionWithSameExpiryDateToInvoiceList(
    List<Product> products,
    Set<StockPosition> PositionTaken,
    Set<Product> productTaken,
    LocalDate expiryDate,
    int quantityLeft,
    int startIndex,
    List<Product> invoiceList
  ) {
    for (
      int index = startIndex;
      index < products.size() && products.get(index).getExpiredDate().equals(expiryDate);
      index++
    ) {
      if (PositionTaken.contains(products.get(index).getStockPosition())) {
        if (quantityLeft > products.get(index).getQuantity()) {
          quantityLeft -= products.get(index).getQuantity();
          invoiceList.add(products.get(index));
          productTaken.add(products.get(index));
        } else {
          Product newProduct = new Product(products.get(index), quantityLeft);
          invoiceList.add(newProduct);
          quantityLeft = 0;
          break;
        }
      }
    }
    return quantityLeft;
  }

  private int addRemainingProductsWithSameExpiryDateToInvoiceList(
    List<Product> products,
    Set<Product> productTaken,
    LocalDate expiryDate,
    int quantityLeft,
    int startIndex,
    List<Product> invoiceList
  ) {
    for (
      int index = startIndex;
      index < products.size() && products.get(index).getExpiredDate().equals(expiryDate);
      index++
    ) {
      if (!productTaken.contains(products.get(index))) {
        if (quantityLeft > products.get(index).getQuantity()) {
          quantityLeft -= products.get(index).getQuantity();
          invoiceList.add(products.get(index));
        } else {
          Product newProduct = new Product(products.get(index), quantityLeft);
          invoiceList.add(newProduct);
          quantityLeft = 0;
          break;
        }
      }
    }
    return quantityLeft;
  }

  @Override
  public void updateProductQuantityInStock(List<Product> invoiceList) {
    List<Product> newProducts = new ArrayList<>();
    List<Product> deleProducts = new ArrayList<>();
    invoiceList.forEach(productInvoice -> {
      Product currProduct = productRepository.findById(productInvoice.getId()).get();
      if (currProduct == null) {
        throw new RuntimeException("Product not found!");
      }
      int newQuantity = currProduct.getQuantity() - productInvoice.getQuantity();
      if (newQuantity > 0) {
        Product newProduct = new Product(currProduct, newQuantity);
        newProducts.add(newProduct);
      } else {
        deleProducts.add(currProduct);
      }
    });
    productRepository.saveAll(newProducts);
    productRepository.deleteAll(deleProducts);
  }

  @Override
  public void importProductProcess(
    List<String> productInfoIds,
    List<LocalDate> expiredDates,
    List<Integer> totalQuantityIntegers,
    List<String> positionIds,
    List<Integer> positionQuantities
  ) {
    int lengthProductInfo = productInfoIds.size();
    int lengthPosition = positionIds.size();
    int indexPosition = 0;

    for (int i = 0; i < lengthProductInfo; i++) {
      int sumQuantity = 0;
      String productInfoId = productInfoIds.get(i);
      LocalDate expiredDate = expiredDates.get(i);
      int totalQuantity = totalQuantityIntegers.get(i);

      while (indexPosition < lengthPosition && sumQuantity < totalQuantity) {
        int quantity = positionQuantities.get(indexPosition);
        String positionId = positionIds.get(indexPosition);
        Product product = updateOrCreateProduct(productInfoId, positionId, expiredDate, quantity);
        productRepository.save(product);
        sumQuantity += quantity;
        indexPosition++;
      }

      if (sumQuantity != totalQuantity) throw new RuntimeException(
        "Quantity of product is not equal to total quantity"
      );
    }
  }

  private Product updateOrCreateProduct(
    String productInfoId,
    String positionId,
    LocalDate expiredDate,
    int quantity
  ) {
    Optional<Product> existedProduct =
      productRepository.findByProductInfoIdAndStockPositionIdAndExpiredDate(
        productInfoId,
        positionId,
        expiredDate
      );

    if (existedProduct.isPresent()) {
      Product product = existedProduct.get();
      product.setQuantity(product.getQuantity() + quantity);
      return product;
    } else {
      return Product.builder()
        .productInfo(
          productInfoRepository
            .findById(productInfoId)
            .orElseThrow(() -> new RuntimeException("Product info not found"))
        )
        .stockPosition(
          stockPositionRepository
            .findById(positionId)
            .orElseThrow(() -> new RuntimeException("Stock position not found"))
        )
        .quantity(quantity)
        .expiredDate(expiredDate)
        .build();
    }
  }

  @Override
  public List<Product> getExpiredProductsByStockId(String stockId) {
    return productRepository.getExpiredProductsByStockId(stockId, LocalDate.now());
  }

  @Override
  public void deleteProductsByIds(List<String> productIds) {
    productIds.forEach(id -> {
      Product product = productRepository.findById(id).orElseThrow();

      // Save destruction history
      DestroyProductHistory history = DestroyProductHistory.builder()
        .productInfo(product.getProductInfo())
        .stock(product.getStockPosition().getStock())
        .expiredDate(product.getExpiredDate())
        .quantity(product.getQuantity())
        .deletedTimestamp(LocalDateTime.now())
        .user(authService.getCurrentUser().get())
        .build();

      destroyProductHistoryRepository.save(history);

      // Delete the product
      productRepository.delete(product);
    });
  }

  @Override
  public List<Product> getProductsByStockPositionId(String stockPositionId) {
    return productRepository.findByStockPositionId(stockPositionId);
  }

  @Override
  public Long getAllProdutsByStockId(String stockId) {
    return productRepository.getAllProdutsByStockId(stockId);
  }

  @Override
  public ProductExpiredStatusDTO getProductExpiredStatusStockId(String stockId) {
    return productRepository.getProductExpiredStatusStockId(stockId, LocalDate.now());
  }

  @Override
  public int countProductsNearExpiry(int days, String stockId) {
    LocalDate now = LocalDate.now();
    LocalDate futureDate = now.plusDays(days);
    return productRepository.countProductsNearExpiry(now, futureDate, stockId);
  }

  @Override
  public List<ProductQuantityInStockTODO> getAllProductQuantityByStockId(String stockId) {
    return productRepository.findProductInventoryByStockIdAndExpiredDateAfter(
      stockId,
      LocalDate.now()
    );
  }
}
