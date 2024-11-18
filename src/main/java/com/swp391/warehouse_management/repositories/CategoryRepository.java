package com.swp391.warehouse_management.repositories;

import com.swp391.warehouse_management.dto.CategorySummariesDTO;
import com.swp391.warehouse_management.entities.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
  boolean existsByName(String name);
  Category findByName(String name);

  @Query(
    """
    SELECT new com.swp391.warehouse_management.dto.CategorySummariesDTO(c, COALESCE(sum(p.quantity), 0) as quantityProducts)
    FROM Category c
    JOIN Product p ON c.id = p.productInfo.category.id
    WHERE p.stockPosition.stock.id =:stockId
    GROUP BY c
    ORDER by quantityProducts DESC
    LIMIT 10
    """
  )
  List<CategorySummariesDTO> getCategorySummariesByStockId(@Param("stockId") String stockId);
}
