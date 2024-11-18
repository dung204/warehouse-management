package com.swp391.warehouse_management.services;

import com.swp391.warehouse_management.dto.StockPositionSummariesDTO;
import com.swp391.warehouse_management.entities.Stock;
import com.swp391.warehouse_management.entities.StockPosition;
import java.util.List;
import java.util.Optional;

public interface StockPositionService {
  Stock getStockById(String id);
  List<StockPosition> getAllStockPositionByStockId(String id);
  List<StockPosition> getAllStockPositions();
  Optional<StockPosition> findByid(String id);
  Boolean create(StockPosition stockPosition);
  Boolean updateStockPositionName(String id, String newName);
  StockPosition saveStock(StockPosition stockPosition);
  void removeStockPosition(String id);
  List<StockPosition> getAllByStockId(String id);
  List<StockPositionSummariesDTO> getPositionSummariesByStockId(String stockId, int limt);
  List<StockPositionSummariesDTO> getAllPositionSummariesByStockId(String stockId);
}
