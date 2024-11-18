package com.swp391.warehouse_management.services;

import com.swp391.warehouse_management.dto.InventoryProductReportDTO;
import com.swp391.warehouse_management.dto.InventoryReportByProductInfoDTO;
import java.time.LocalDate;
import java.util.List;

public interface InventoryProductService {
  List<InventoryProductReportDTO> getInventoryProductReport(
    LocalDate fromDate,
    LocalDate toDate,
    String stockId
  );

  List<InventoryReportByProductInfoDTO> getInventoryReportByProductInfo(
    LocalDate fromDate,
    LocalDate toDate,
    String stockId,
    String productInfoId
  );
}
