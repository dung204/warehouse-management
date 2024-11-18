package com.swp391.warehouse_management.services.impl;

import com.swp391.warehouse_management.entities.Stock;
import com.swp391.warehouse_management.repositories.StockRepository;
import com.swp391.warehouse_management.services.StockService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements StockService {

  @Autowired
  private StockRepository stockRepository;

  @Override
  public Optional<Stock> findByid(String id) {
    return stockRepository.findById(id);
  }

  @Override
  public List<Stock> getAllStock() {
    return stockRepository.findAll();
  }

  @Override
  @Transactional
  public Stock saveStock(Stock stock) {
    return stockRepository.save(stock);
  }

  @Override
  public Boolean create(Stock stock) {
    try {
      this.stockRepository.save(stock);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public Boolean updateStock(Stock stock) {
    try {
      this.stockRepository.save(stock);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public List<Stock> getAllStockExceptId(String id) {
    return stockRepository.findByIdNot(id);
  }

  @Override
  public List<Stock> getAllStocks() {
    return stockRepository.findAll();
  }
}
