package com.swp391.warehouse_management.services;

import com.swp391.warehouse_management.dto.CategorySummariesDTO;
import com.swp391.warehouse_management.entities.Category;
import java.util.List;

public interface CategoryService {
  List<Category> getAllCategories();

  List<Category> getCategoriesById(List<String> id);

  Category getCategoryByName(String name);
  boolean existsByName(String name);
  void saveCategory(Category category);
  List<Category> getAllCategory();
  List<CategorySummariesDTO> getCategorySummariesByStockId(String stockId);
}
