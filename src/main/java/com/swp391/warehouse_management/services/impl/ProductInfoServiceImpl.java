package com.swp391.warehouse_management.services.impl;

import com.swp391.warehouse_management.dto.ProductSummariesDTO;
import com.swp391.warehouse_management.entities.ProductInfo;
import com.swp391.warehouse_management.repositories.ProductInfoRepository;
import com.swp391.warehouse_management.services.ProductInfoService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductInfoServiceImpl implements ProductInfoService {

  private final ProductInfoRepository productInfoRepository;

  @Override
  public List<ProductInfo> getAllProductInfos() {
    return productInfoRepository.findAllAndSortByName();
  }

  @Override
  public void saveProductInfo(ProductInfo productInfo) {
    productInfoRepository.save(productInfo);
  }

  @Override
  public ProductInfo getProductInfoByName(String name) {
    return productInfoRepository.findByName(name);
  }

  @Override
  public boolean existsByName(String name) {
    return productInfoRepository.existsByName(name);
  }

  @Override
  public List<ProductSummariesDTO> getProductSummariesByStockId(String stockId) {
    return productInfoRepository.getProductSummariesByStockId(stockId);
  }

  @Override
  public Optional<ProductInfo> getProductInfoById(String id) {
    return productInfoRepository.findById(id);
  }
}
