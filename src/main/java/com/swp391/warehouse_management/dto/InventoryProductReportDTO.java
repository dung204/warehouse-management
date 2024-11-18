package com.swp391.warehouse_management.dto;

import com.swp391.warehouse_management.entities.ProductInfo;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InventoryProductReportDTO {

  private ProductInfo productInfo;
  private Long beginningQuantity;
  private Long importedQuantity;
  private Long exportedQuantity;
  private Long destroyedQuantity;
  private Long endingQuantity;
}
