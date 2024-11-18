package com.swp391.warehouse_management.services.impl;

import com.swp391.warehouse_management.entities.ImportOrder;
import com.swp391.warehouse_management.entities.ImportOrderDetails;
import com.swp391.warehouse_management.entities.ProductInfo;
import com.swp391.warehouse_management.repositories.ImportOrderDetailsRepository;
import com.swp391.warehouse_management.repositories.ProductInfoRepository;
import com.swp391.warehouse_management.services.ImportOrderDetailsService;
import java.time.LocalDate;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImportOrderDetailsServiceImpl implements ImportOrderDetailsService {

  private final ImportOrderDetailsRepository importOrderDetailsRepository;
  private final ProductInfoRepository productInfoRepository;

  @Override
  public List<ImportOrderDetails> createImportOrderDetails(
    ImportOrder importOrder,
    List<String> productInfoIds,
    List<LocalDate> expiredDates,
    List<Integer> totalQuantityIntegers
  ) {
    Map<SimpleEntry<ProductInfo, LocalDate>, Integer> groupedDetails = new HashMap<>();

    for (int i = 0; i < productInfoIds.size(); i++) {
      ProductInfo productInfo = productInfoRepository.findById(productInfoIds.get(i)).get();
      LocalDate expiredDate = expiredDates.get(i);
      int quantity = totalQuantityIntegers.get(i);
      SimpleEntry<ProductInfo, LocalDate> key = new SimpleEntry<>(productInfo, expiredDate);
      groupedDetails.merge(key, quantity, Integer::sum);
    }

    List<ImportOrderDetails> importOrderDetails = new ArrayList<>();
    for (Map.Entry<
      SimpleEntry<ProductInfo, LocalDate>,
      Integer
    > entry : groupedDetails.entrySet()) {
      ImportOrderDetails importOrderDetail = ImportOrderDetails.builder()
        .importOrder(importOrder)
        .productInfo(entry.getKey().getKey())
        .expiredDate(entry.getKey().getValue())
        .quantity(entry.getValue())
        .build();
      importOrderDetails.add(importOrderDetail);
    }
    return importOrderDetailsRepository.saveAll(importOrderDetails);
  }

  @Override
  public List<ImportOrderDetails> getByimportOrderId(String id) {
    return importOrderDetailsRepository.findByimportOrderId(id);
  }

  @Override
  public Long getAllImportProdutsByStockId(String stockId) {
    return importOrderDetailsRepository.getAllImportProdutsByStockId(stockId);
  }
}
