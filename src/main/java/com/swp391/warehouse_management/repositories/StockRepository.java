package com.swp391.warehouse_management.repositories;

import com.swp391.warehouse_management.entities.Stock;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {
  @Query("SELECT s FROM Stock s WHERE s.id <> ?1 ORDER BY s.name ASC")
  List<Stock> findByIdNot(String id);
}
