package com.swp391.warehouse_management.controllers;

import com.swp391.warehouse_management.common.utils.AppRoutes;
import com.swp391.warehouse_management.entities.Category;
import com.swp391.warehouse_management.services.CategoryService;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping(AppRoutes.LIST_CATEGORIES)
  public String categoryPage(Model model) {
    model.addAttribute("pageTitle", "Category Management");
    model.addAttribute("fieldNames", List.of("Name"));
    model.addAttribute("categories", categoryService.getAllCategories());
    return "pages/list-categories";
  }

  @GetMapping(AppRoutes.ADD_CATEGORY)
  public String addCategoryPage(Model model) {
    model.addAttribute("pageTitle", "Add New Category");
    model.addAttribute("category", new Category());
    return "pages/add-category";
  }

  @PostMapping(AppRoutes.ADD_CATEGORY)
  public String addCategory(@ModelAttribute Category category, Model model) {
    if (categoryService.existsByName(category.getName())) {
      model.addAttribute("errorMessage", "Category name already exists");
      model.addAttribute("category", category);
      return "pages/add-category";
    }
    categoryService.saveCategory(category);
    return "redirect:" + AppRoutes.LIST_CATEGORIES;
  }

  @GetMapping(AppRoutes.EDIT_CATEGORY)
  public String editCategoryPage(@PathVariable String id, Model model) {
    Category category = categoryService.getCategoriesById(Collections.singletonList(id)).get(0);
    if (category == null) {
      return "redirect:" + AppRoutes.LIST_CATEGORIES;
    }
    model.addAttribute("pageTitle", "Edit Category");
    model.addAttribute("category", category);
    return "pages/edit-category";
  }

  @PostMapping(AppRoutes.EDIT_CATEGORY_POST)
  public String editCategory(@ModelAttribute Category category, Model model) {
    Category existingCategory = categoryService.getCategoryByName(category.getName());
    if (existingCategory != null && !existingCategory.getId().equals(category.getId())) {
      model.addAttribute("errorMessage", "Category name already exists");
      model.addAttribute("category", category);
      return "pages/edit-category";
    }
    // Ensure the ID is preserved
    Category categoryToUpdate = categoryService
      .getCategoriesById(Collections.singletonList(category.getId()))
      .get(0);
    if (categoryToUpdate != null) {
      categoryToUpdate.setName(category.getName());
      categoryService.saveCategory(categoryToUpdate);
    } else {
      categoryService.saveCategory(category);
    }
    return "redirect:" + AppRoutes.LIST_CATEGORIES;
  }
}
