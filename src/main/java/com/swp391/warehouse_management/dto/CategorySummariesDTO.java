package com.swp391.warehouse_management.dto;

import com.swp391.warehouse_management.entities.Category;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategorySummariesDTO {

  private Category category;
  private long quantityProducts;
}
