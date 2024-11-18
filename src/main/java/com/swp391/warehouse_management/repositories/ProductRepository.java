package com.swp391.warehouse_management.repositories;

import com.swp391.warehouse_management.dto.ProductExpiredStatusDTO;
import com.swp391.warehouse_management.dto.ProductQuantityInStockTODO;
import com.swp391.warehouse_management.entities.Product;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
  @Query(
    "SELECT p FROM Product p WHERE p.stockPosition.stock.id = :stockId AND p.expiredDate > :expiredDate"
  )
  List<Product> findByStockIdAndExpiredDateAfter(
    @Param("stockId") String stockId,
    @Param("expiredDate") LocalDate expiredDate
  );

  @Query(
    "SELECT p FROM Product p WHERE p.stockPosition.id = :stockPositionId AND p.expiredDate > :expiredDate"
  )
  List<Product> findByStockPositionIdAndExpiredDateAfter(
    @Param("stockPositionId") String stockPositionId,
    @Param("expiredDate") LocalDate expiredDate
  );

  @Query(
    "SELECT p FROM Product p WHERE p.stockPosition.id = :stockPositionId AND p.expiredDate <= :expiredDate"
  )
  List<Product> findByStockPositionIdAndExpiredDateBeforeOrEqual(
    @Param("stockPositionId") String stockPositionId,
    @Param("expiredDate") LocalDate expiredDate
  );

  @Query("SELECT count(p) FROM Product p WHERE p.stockPosition.id = :id")
  int countByPositionId(String id);

  @Query(
    "SELECT p FROM Product p WHERE p.productInfo.id = :productInfoId AND p.stockPosition.id = :stockPositionId AND  p.expiredDate = :expiredDate"
  )
  Optional<Product> findByProductInfoIdAndStockPositionIdAndExpiredDate(
    @Param("productInfoId") String productInfoId,
    @Param("stockPositionId") String stockPositionId,
    @Param("expiredDate") LocalDate expiredDate
  );

  @Query(
    """
    SELECT new com.swp391.warehouse_management.dto.ProductQuantityInStockTODO(p.productInfo, SUM(p.quantity))
    FROM Product p
    WHERE p.stockPosition.stock.id = :stockId
    AND p.expiredDate > :expiredDate
    GROUP BY p.productInfo
    ORDER BY p.productInfo.name
    """
  )
  List<ProductQuantityInStockTODO> findProductInventoryByStockIdAndExpiredDateAfter(
    @Param("stockId") String stockId,
    @Param("expiredDate") LocalDate expiredDate
  );

  @Query(
    """
    SELECT p FROM Product p
    WHERE p.stockPosition.stock.id = :stockId
    AND p.expiredDate > :expiredDate
    AND p.productInfo.id = :productInfoId
    ORDER BY p.expiredDate ASC, p.quantity DESC
    """
  )
  List<Product> findbyStockIdAndProductInfoIdAndExpiredDateAfter(
    @Param("stockId") String stockId,
    @Param("productInfoId") String productInfoId,
    @Param("expiredDate") LocalDate expiredDate
  );

  @Query(
    "SELECT p FROM Product p WHERE p.stockPosition.stock.id = :stockId AND p.expiredDate <= :expiredDate"
  )
  List<Product> getExpiredProductsByStockId(
    @Param("stockId") String stockId,
    @Param("expiredDate") LocalDate expiredDate
  );

  @Query("SELECT p FROM Product p WHERE p.stockPosition.id = :stockPositionId")
  List<Product> findByStockPositionId(String stockPositionId);

  @Query(
    """
    SELECT COUNT(p.id) FROM Product p
    WHERE p.stockPosition.stock.id = ?1
    """
  )
  Long getAllProdutsByStockId(String stockId);

  @Query(
    """
    SELECT new com.swp391.warehouse_management.dto.ProductExpiredStatusDTO(
                COUNT(CASE WHEN p.expiredDate >  :currentDate THEN p.id ELSE NULL END) as unexpiredProducts,
                COUNT(CASE WHEN p.expiredDate <= :currentDate THEN p.id ELSE NULL END) as expiredProducts)
      FROM Product p
      WHERE p.stockPosition.stock.id =:stockId
      """
  )
  ProductExpiredStatusDTO getProductExpiredStatusStockId(
    @Param("stockId") String stockId,
    @Param("currentDate") LocalDate currentDate
  );

  @Query(
    """
    SELECT COUNT(p.id)
    FROM Product p
    WHERE p.expiredDate > :currentDate
    AND p.expiredDate <= :futureDate
    AND p.stockPosition.stock.id =:stockId
    """
  )
  int countProductsNearExpiry(
    @Param("currentDate") LocalDate currentDate,
    @Param("futureDate") LocalDate futureDate,
    @Param("stockId") String stockId
  );
}
