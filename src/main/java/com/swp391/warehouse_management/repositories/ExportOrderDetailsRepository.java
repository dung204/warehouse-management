package com.swp391.warehouse_management.repositories;

import com.swp391.warehouse_management.entities.ExportOrderDetails;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExportOrderDetailsRepository extends JpaRepository<ExportOrderDetails, String> {
  @Query(
    """
    SELECT e FROM ExportOrderDetails e
    WHERE e.exportOrder.id = :orderId
    """
  )
  List<ExportOrderDetails> findAllByExportOrderId(String orderId);

  @Query(
    """
    SELECT eod FROM ExportOrderDetails eod
    WHERE eod.productInfo.id = ?2
    AND eod.exportOrder.sourceStock.id = ?1
    AND eod.exportOrder.createdTimestamp >= ?3
    AND eod.exportOrder.createdTimestamp <= ?4
    """
  )
  List<ExportOrderDetails> findBySourceStockIdAndProductInfoIdAndCreatedTimestampBetween(
    String stockId,
    String productInfoId,
    LocalDateTime from,
    LocalDateTime to
  );

  @Query(
    """
    SELECT COUNT(eod.id) FROM ExportOrderDetails eod
    WHERE eod.exportOrder.sourceStock.id = ?1
    """
  )
  Long getAllExportProdutsByStockId(String stockId);
}
