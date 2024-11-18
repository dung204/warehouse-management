package com.swp391.warehouse_management.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductExpiredStatusDTO {

  private long unexpiredProducts;
  private long expiredProducts;
}
