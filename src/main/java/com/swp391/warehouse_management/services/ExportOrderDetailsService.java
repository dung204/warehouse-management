package com.swp391.warehouse_management.services;

import com.swp391.warehouse_management.entities.ExportOrder;
import com.swp391.warehouse_management.entities.ExportOrderDetails;
import com.swp391.warehouse_management.entities.Product;
import java.util.List;

public interface ExportOrderDetailsService {
  List<ExportOrderDetails> getByExportOrderId(String id);
  void addProductsToExportOrderDetail(ExportOrder exportOrder, List<Product> invoiceList);
  Long getAllExportProdutsByStockId(String stockId);
}
