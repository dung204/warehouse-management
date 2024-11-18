package com.swp391.warehouse_management.services;

import com.swp391.warehouse_management.entities.DestroyProductHistory;
import java.util.List;

public interface DestroyProductHistoryService {
  List<DestroyProductHistory> getDestroyProductHistoryByStockId(String stockId);
  Long getAllDestroyProductHistoryByStockId(String stockId);
}
