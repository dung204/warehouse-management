package com.swp391.warehouse_management.services.impl;

import com.swp391.warehouse_management.common.enums.OrderStatus;
import com.swp391.warehouse_management.entities.ExportOrder;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.repositories.ExportOrderRepository;
import com.swp391.warehouse_management.services.ExportOrderService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExportOrderServiceImpl implements ExportOrderService {

  private final ExportOrderRepository exportOrderRepository;

  @Override
  public ExportOrder createExportOrder(String pickerName, User currUser) {
    ExportOrder exportOrder = ExportOrder.builder()
      .exportUser(currUser)
      .pickerName(pickerName)
      .sourceStock(currUser.getStock())
      .status(OrderStatus.CONFIRMED)
      .build();
    return exportOrderRepository.save(exportOrder);
  }

  @Override
  public List<ExportOrder> checkExportOrderStatusByStockId(String stockId) {
    return exportOrderRepository.findByDestinationStockIdAndStatus(stockId, OrderStatus.PENDING);
  }

  @Override
  public ExportOrder getById(String id) {
    return exportOrderRepository.findById(id).get();
  }

  @Override
  public void changeStatus(ExportOrder exportOrder, OrderStatus stauts) {
    exportOrder.setStatus(stauts);
    exportOrderRepository.save(exportOrder);
  }

  @Override
  public List<ExportOrder> getAllByStockId(String stockId) {
    return exportOrderRepository.findAllByStockId(stockId);
  }
}
