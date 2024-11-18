package com.swp391.warehouse_management.repositories;

import com.swp391.warehouse_management.dto.ProductSummariesDTO;
import com.swp391.warehouse_management.entities.ProductInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {
  boolean existsByName(String name);
  ProductInfo findByName(String name);

  @Query(
    """
    SELECT new com.swp391.warehouse_management.dto.ProductSummariesDTO(pi, COALESCE(sum(p.quantity), 0) as totalQuantity)
    FROM ProductInfo pi
    JOIN Product p ON pi.id = p.productInfo.id
    WHERE p.stockPosition.stock.id =:stockId
    GROUP BY pi
    ORDER by totalQuantity DESC
    LIMIT 12
    """
  )
  List<ProductSummariesDTO> getProductSummariesByStockId(@Param("stockId") String stockId);

  @Query(
    """
    SELECT pi
    FROM ProductInfo pi
    ORDER by pi.name
    """
  )
  List<ProductInfo> findAllAndSortByName();
}
