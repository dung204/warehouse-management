package com.swp391.warehouse_management.services;

import com.swp391.warehouse_management.common.enums.OrderStatus;
import com.swp391.warehouse_management.entities.ExportOrder;
import com.swp391.warehouse_management.entities.User;
import java.util.List;

public interface ExportOrderService {
  ExportOrder getById(String id);
  void changeStatus(ExportOrder exportOrder, OrderStatus stauts);
  ExportOrder createExportOrder(String pickerName, User currUser);
  List<ExportOrder> checkExportOrderStatusByStockId(String stockId);
  List<ExportOrder> getAllByStockId(String stockId);
}
