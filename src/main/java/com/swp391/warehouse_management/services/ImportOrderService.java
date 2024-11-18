package com.swp391.warehouse_management.services;

import com.swp391.warehouse_management.entities.ImportOrder;
import com.swp391.warehouse_management.entities.Stock;
import com.swp391.warehouse_management.entities.User;
import java.util.List;

public interface ImportOrderService {
  ImportOrder createImportOrder(String deliverName, User user);
  ImportOrder createImportOrder(Stock sourceStock, User importUser);
  ImportOrder getById(String id);
  List<ImportOrder> getAllByStockId(String stockId);
}
