package com.swp391.warehouse_management.services.impl;

import com.swp391.warehouse_management.entities.ImportOrder;
import com.swp391.warehouse_management.entities.Stock;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.repositories.ImportOrderRepository;
import com.swp391.warehouse_management.services.ImportOrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImportOrderServiceImpl implements ImportOrderService {

  private final ImportOrderRepository importOrderRepository;

  @Override
  public ImportOrder createImportOrder(String deliverName, User user) {
    ImportOrder importOrder = ImportOrder.builder()
      .deliverName(deliverName)
      .importUser(user)
      .destinationStock(user.getStock())
      .build();
    return importOrderRepository.save(importOrder);
  }

  @Override
  public ImportOrder createImportOrder(Stock sourceStock, User importUser) {
    ImportOrder importOrder = ImportOrder.builder()
      .sourceStock(sourceStock)
      .importUser(importUser)
      .destinationStock(importUser.getStock())
      .build();
    return importOrderRepository.save(importOrder);
  }

  @Override
  public ImportOrder getById(String id) {
    return importOrderRepository.findById(id).get();
  }

  @Override
  public List<ImportOrder> getAllByStockId(String stockId) {
    return importOrderRepository.findAllByStockId(stockId);
  }
}
