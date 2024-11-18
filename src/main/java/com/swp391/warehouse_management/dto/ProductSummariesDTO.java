package com.swp391.warehouse_management.dto;

import com.swp391.warehouse_management.entities.ProductInfo;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductSummariesDTO {

  private ProductInfo productInfo;
  private long totalQuantity;
}
