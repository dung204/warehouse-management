package com.swp391.warehouse_management.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InventoryReportByProductInfoDTO {

  private LocalDate date;
  private Long inventoryQuantity;
  private Long importedQuantity;
  private Long exportedQuantity;
  private Long destroyedQuantity;
}
