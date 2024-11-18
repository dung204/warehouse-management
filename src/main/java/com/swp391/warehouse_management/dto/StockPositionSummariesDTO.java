package com.swp391.warehouse_management.dto;

import com.swp391.warehouse_management.entities.StockPosition;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StockPositionSummariesDTO {

  private StockPosition stockPosition;
  private Long unexpiredProducts;
  private Long expiredProducts;

  public Long getTotalProducts() {
    return unexpiredProducts + expiredProducts;
  }

  public int getUnexpiredPercent() {
    long totalProducts = getTotalProducts();
    return totalProducts != 0 ? (int) ((unexpiredProducts * 100) / totalProducts) : -1;
  }
}
