package com.swp391.warehouse_management.repositories;

import com.swp391.warehouse_management.entities.DestroyProductHistory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DestroyProductHistoryRepository
  extends JpaRepository<DestroyProductHistory, String> {
  @Query("SELECT d FROM DestroyProductHistory d WHERE d.stock.id = :stockId")
  List<DestroyProductHistory> findByStockId(@Param("stockId") String stockId);

  @Query(
    """
    SELECT d FROM DestroyProductHistory d
    WHERE d.productInfo.id = :productInfoId
    AND d.stock.id = :stockId
    AND d.deletedTimestamp >= :from
    AND d.deletedTimestamp <= :to
    """
  )
  List<DestroyProductHistory> findByProductInfoIdAndStockIdAndDeletedTimestampBetween(
    @Param("productInfoId") String productInfoId,
    @Param("stockId") String stockId,
    @Param("from") LocalDateTime from,
    @Param("to") LocalDateTime to
  );

  @Query(
    """
    SELECT COUNT(d.id) FROM DestroyProductHistory d
    WHERE d.stock.id = ?1
    """
  )
  Long getAllDestroyProductHistoryByStockId(String stockId);
}
