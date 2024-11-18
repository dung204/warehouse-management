package com.swp391.warehouse_management.services.impl;

import com.swp391.warehouse_management.dto.StockPositionSummariesDTO;
import com.swp391.warehouse_management.entities.Stock;
import com.swp391.warehouse_management.entities.StockPosition;
import com.swp391.warehouse_management.repositories.StockPositionRepository;
import com.swp391.warehouse_management.services.StockPositionService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockPositionServiceImpl implements StockPositionService {

  private final StockPositionRepository stockPositionRepository;

  @Override
  public List<StockPosition> getAllStockPositionByStockId(String id) {
    return stockPositionRepository.findByStockId(id);
  }

  @Override
  public Boolean create(StockPosition stockPosition) {
    try {
      this.stockPositionRepository.save(stockPosition);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public Boolean updateStockPositionName(String id, String newName) {
    try {
      Optional<StockPosition> optionalStockPosition = stockPositionRepository.findById(id);

      if (optionalStockPosition.isPresent()) {
        StockPosition stockPosition = optionalStockPosition.get();
        stockPosition.setName(newName);
        stockPositionRepository.save(stockPosition);
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  @Transactional
  public StockPosition saveStock(StockPosition stockPosition) {
    return stockPositionRepository.save(stockPosition);
  }

  @Override
  public List<StockPosition> getAllStockPositions() {
    return stockPositionRepository.findAll();
  }

  @Override
  public Optional<StockPosition> findByid(String id) {
    return stockPositionRepository.findById(id);
  }

  @Override
  public Stock getStockById(String id) {
    return stockPositionRepository.getStockById(id);
  }

  @Override
  public void removeStockPosition(String id) {
    stockPositionRepository.deleteById(id);
  }

  @Override
  public List<StockPosition> getAllByStockId(String id) {
    return this.stockPositionRepository.findByStockId(id);
  }

  @Override
  public List<StockPositionSummariesDTO> getPositionSummariesByStockId(String stockId, int limt) {
    return stockPositionRepository.getPositionSummariesByStockId(stockId, LocalDate.now(), limt);
  }

  @Override
  public List<StockPositionSummariesDTO> getAllPositionSummariesByStockId(String stockId) {
    return stockPositionRepository.getPositionSummariesByStockId(
      stockId,
      LocalDate.now(),
      Integer.MAX_VALUE
    );
  }
}
