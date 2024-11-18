package com.swp391.warehouse_management.services;

import com.swp391.warehouse_management.dto.ProductSummariesDTO;
import com.swp391.warehouse_management.entities.ProductInfo;
import java.util.List;
import java.util.Optional;

public interface ProductInfoService {
  List<ProductInfo> getAllProductInfos();
  void saveProductInfo(ProductInfo productInfo);
  boolean existsByName(String name);
  ProductInfo getProductInfoByName(String name);
  List<ProductSummariesDTO> getProductSummariesByStockId(String stockId);
  Optional<ProductInfo> getProductInfoById(String id);
}
