package com.swp391.warehouse_management.repositories;

import com.swp391.warehouse_management.dto.StockPositionSummariesDTO;
import com.swp391.warehouse_management.entities.Stock;
import com.swp391.warehouse_management.entities.StockPosition;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockPositionRepository extends JpaRepository<StockPosition, String> {
  @Query("SELECT p.stock FROM StockPosition p WHERE p.id = :id")
  Stock getStockById(String id);

  @Query("SELECT sp FROM StockPosition sp WHERE sp.stock.id = :id ORDER BY sp.name")
  List<StockPosition> findByStockId(String id);

  @Query(
    """
    SELECT new com.swp391.warehouse_management.dto.StockPositionSummariesDTO(
               sp,
               COUNT(CASE WHEN p.expiredDate >  :currentDate THEN p.id ELSE NULL END) as unexpiredProducts,
               COUNT(CASE WHEN p.expiredDate <= :currentDate THEN p.id ELSE NULL END) as expiredProducts)
    FROM StockPosition sp
    LEFT JOIN Product p ON p.stockPosition = sp
    WHERE sp.stock.id =:stockId
    GROUP BY sp
    ORDER BY (COUNT(CASE WHEN p.expiredDate > :currentDate THEN p.id ELSE NULL END) * 100.0) / COUNT(p.id)  NULLS LAST,
             expiredProducts DESC, unexpiredProducts DESC
    LIMIT :limit
    """
  )
  List<StockPositionSummariesDTO> getPositionSummariesByStockId(
    @Param("stockId") String stockId,
    @Param("currentDate") LocalDate currentDate,
    @Param("limit") int limit
  );
}
