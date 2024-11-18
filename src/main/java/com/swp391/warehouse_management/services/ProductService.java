package com.swp391.warehouse_management.services;

import com.swp391.warehouse_management.dto.ProductExpiredStatusDTO;
import com.swp391.warehouse_management.dto.ProductQuantityInStockTODO;
import com.swp391.warehouse_management.entities.Product;
import java.time.LocalDate;
import java.util.List;

public interface ProductService {
  List<Product> getUnexpiredProductsByStockId(String stockId);

  int getNumberOfProductsByPositionId(String stockId);

  void changePositionInternalByProductId(
    String productId,
    List<String> positions,
    List<Integer> quantities
  );

  void moveProductsToAnotherStock(
    String destinationStockId,
    List<String> productIds,
    List<Integer> quantities
  );
  List<Product> getInvoiceListByStockId(
    String stockId,
    List<String> productInfoIdList,
    List<Integer> quantities
  );
  void updateProductQuantityInStock(List<Product> invoiceList);
  void importProductProcess(
    List<String> productInfoIds,
    List<LocalDate> expiredDates,
    List<Integer> totalQuantityIntegers,
    List<String> positionIds,
    List<Integer> positionQuantities
  );
  List<Product> getExpiredProductsByStockId(String stockId);
  void deleteProductsByIds(List<String> productIds);

  List<Product> getProductsByStockPositionId(String stockPositionId);
  Long getAllProdutsByStockId(String stockId);
  ProductExpiredStatusDTO getProductExpiredStatusStockId(String stockId);
  int countProductsNearExpiry(int days, String stockId);
  List<ProductQuantityInStockTODO> getAllProductQuantityByStockId(String stockId);
}
