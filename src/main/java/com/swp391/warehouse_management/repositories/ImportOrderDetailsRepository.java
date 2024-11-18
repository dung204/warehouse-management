package com.swp391.warehouse_management.repositories;

import com.swp391.warehouse_management.entities.ImportOrderDetails;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImportOrderDetailsRepository extends JpaRepository<ImportOrderDetails, String> {
  @Query(
    """
    SELECT o FROM ImportOrderDetails o
    WHERE o.importOrder.id = :id
    """
  )
  List<ImportOrderDetails> findByimportOrderId(String id);

  @Query(
    """
    SELECT iod FROM ImportOrderDetails iod
    WHERE iod.productInfo.id = ?2
    AND iod.importOrder.destinationStock.id = ?1
    AND iod.importOrder.createdTimestamp >= ?3
    AND iod.importOrder.createdTimestamp <= ?4
    """
  )
  List<ImportOrderDetails> findByDestinationStockIdAndProductInfoIdAndCreatedTimestampBetween(
    String stockId,
    String productInfoId,
    LocalDateTime from,
    LocalDateTime to
  );

  @Query(
    """
    SELECT COUNT(iod.id) FROM ImportOrderDetails iod
    WHERE iod.importOrder.destinationStock.id = ?1
    """
  )
  Long getAllImportProdutsByStockId(String stockId);
}
