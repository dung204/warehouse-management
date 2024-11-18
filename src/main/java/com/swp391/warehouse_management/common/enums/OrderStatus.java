package com.swp391.warehouse_management.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
  PENDING("PENDING"),
  CONFIRMED("CONFIRMED");

  private final String name;
}
