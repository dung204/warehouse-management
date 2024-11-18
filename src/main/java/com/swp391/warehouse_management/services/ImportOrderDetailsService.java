package com.swp391.warehouse_management.services;

import com.swp391.warehouse_management.entities.ImportOrder;
import com.swp391.warehouse_management.entities.ImportOrderDetails;
import java.time.LocalDate;
import java.util.List;

public interface ImportOrderDetailsService {
  List<ImportOrderDetails> createImportOrderDetails(
    ImportOrder importOrder,
    List<String> productInfoIds,
    List<LocalDate> expiredDates,
    List<Integer> totalQuantityIntegers
  );
  List<ImportOrderDetails> getByimportOrderId(String id);
  Long getAllImportProdutsByStockId(String stockId);
}
