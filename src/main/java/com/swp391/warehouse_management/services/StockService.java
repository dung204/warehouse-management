package com.swp391.warehouse_management.services;

import com.swp391.warehouse_management.entities.Stock;
import java.util.List;
import java.util.Optional;

public interface StockService {
  List<Stock> getAllStock();
  Optional<Stock> findByid(String id);
  Stock saveStock(Stock stock);
  Boolean create(Stock stock);
  Boolean updateStock(Stock stock);

  public List<Stock> getAllStocks();

  List<Stock> getAllStockExceptId(String id);
}
