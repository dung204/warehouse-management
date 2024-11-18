package com.swp391.warehouse_management.repositories;

import com.swp391.warehouse_management.entities.InventoryProduct;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryProductRepository extends JpaRepository<InventoryProduct, Long> {
  @Query(
    "SELECT ip FROM InventoryProduct ip WHERE ip.createdDate <= ?1 AND ip.productInfo.id = ?2 AND ip.stock.id = ?3 ORDER BY ip.createdDate DESC LIMIT 1"
  )
  Optional<InventoryProduct> findMostRecentByCreatedDateAndProductInfoIdAndStockId(
    LocalDate createdDate,
    String productInfoId,
    String stockId
  );
}
