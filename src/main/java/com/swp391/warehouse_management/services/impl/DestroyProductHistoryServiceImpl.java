package com.swp391.warehouse_management.services.impl;

import com.swp391.warehouse_management.entities.DestroyProductHistory;
import com.swp391.warehouse_management.repositories.DestroyProductHistoryRepository;
import com.swp391.warehouse_management.services.DestroyProductHistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DestroyProductHistoryServiceImpl implements DestroyProductHistoryService {

  @Autowired
  private DestroyProductHistoryRepository destroyProductHistoryRepository;

  @Override
  public List<DestroyProductHistory> getDestroyProductHistoryByStockId(String stockId) {
    return destroyProductHistoryRepository.findByStockId(stockId);
  }

  @Override
  public Long getAllDestroyProductHistoryByStockId(String stockId) {
    return destroyProductHistoryRepository.getAllDestroyProductHistoryByStockId(stockId);
  }
}
