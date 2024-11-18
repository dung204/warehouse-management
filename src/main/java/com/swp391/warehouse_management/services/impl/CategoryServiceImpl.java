package com.swp391.warehouse_management.services.impl;

import com.swp391.warehouse_management.dto.CategorySummariesDTO;
import com.swp391.warehouse_management.entities.Category;
import com.swp391.warehouse_management.repositories.CategoryRepository;
import com.swp391.warehouse_management.services.CategoryService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  public List<Category> getCategoriesById(List<String> id) {
    return id
      .stream()
      .map(categoryId -> categoryRepository.findById(categoryId).orElse(null))
      .filter(category -> category != null)
      .collect(Collectors.toList());
  }

  @Override
  public Category getCategoryByName(String name) {
    return categoryRepository.findByName(name);
  }

  @Override
  public boolean existsByName(String name) {
    return categoryRepository.existsByName(name);
  }

  @Override
  public void saveCategory(Category category) {
    if (category.getId() != null && categoryRepository.existsById(category.getId())) {
      // Update existing category
      Category existingCategory = categoryRepository
        .findById(category.getId())
        .orElseThrow(() -> new RuntimeException("Category not found"));
      existingCategory.setName(category.getName());
      categoryRepository.save(existingCategory);
    } else {
      // Save new category
      categoryRepository.save(category);
    }
  }

  public List<Category> getAllCategory() {
    return categoryRepository.findAll();
  }

  @Override
  public List<CategorySummariesDTO> getCategorySummariesByStockId(String stockId) {
    return categoryRepository.getCategorySummariesByStockId(stockId);
  }
}
