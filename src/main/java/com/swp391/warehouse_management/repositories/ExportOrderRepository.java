package com.swp391.warehouse_management.repositories;

import com.swp391.warehouse_management.common.enums.OrderStatus;
import com.swp391.warehouse_management.entities.ExportOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExportOrderRepository extends JpaRepository<ExportOrder, String> {
  @Query(
    """
    SELECT e FROM ExportOrder e
    WHERE e.destinationStock.id = :stockId
    AND e.status = :status
    """
  )
  List<ExportOrder> findByDestinationStockIdAndStatus(String stockId, OrderStatus status);

  @Query(
    """
    SELECT e FROM ExportOrder e
    WHERE e.sourceStock.id = :stockId
    ORDER BY e.createdTimestamp DESC
    """
  )
  List<ExportOrder> findAllByStockId(String stockId);
}
