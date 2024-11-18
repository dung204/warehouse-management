package com.swp391.warehouse_management.repositories;

import com.swp391.warehouse_management.entities.ImportOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImportOrderRepository extends JpaRepository<ImportOrder, String> {
  @Query(
    """
    SELECT o FROM ImportOrder o
    WHERE o.destinationStock.id = :id
    Order by o.createdTimestamp DESC
    """
  )
  List<ImportOrder> findAllByStockId(String id);
}
