package com.swp391.warehouse_management.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
  STOCKER("STOCKER"),
  ADMIN("ADMIN");

  private final String name;
}
