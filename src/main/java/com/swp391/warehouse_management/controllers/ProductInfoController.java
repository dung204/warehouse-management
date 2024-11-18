package com.swp391.warehouse_management.controllers;

import com.swp391.warehouse_management.common.utils.AppRoutes;
import com.swp391.warehouse_management.entities.ProductInfo;
import com.swp391.warehouse_management.services.CategoryService;
import com.swp391.warehouse_management.services.ProductInfoService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ProductInfoController {

  private final ProductInfoService productInfoService;
  private final CategoryService categoryService;

  @GetMapping(AppRoutes.LIST_PRODUCT_INFOS)
  public String productInfoPage(Model model) {
    model.addAttribute("pageTitle", "Product Management");
    model.addAttribute("productInfos", productInfoService.getAllProductInfos());
    return "pages/list-product-info";
  }

  @GetMapping(AppRoutes.ADD_PRODUCT_INFO)
  public String addProductInfoPage(Model model) {
    model.addAttribute("pageTitle", "Add New Product Info");
    model.addAttribute("productInfo", new ProductInfo());
    model.addAttribute("categories", categoryService.getAllCategories());
    return "pages/add-product-info";
  }

  @PostMapping(AppRoutes.ADD_PRODUCT_INFO)
  public String addProductInfo(@ModelAttribute ProductInfo productInfo, Model model) {
    if (productInfoService.existsByName(productInfo.getName())) {
      model.addAttribute("errorMessage", "Product name already exists");
      model.addAttribute("productInfo", productInfo);
      model.addAttribute("categories", categoryService.getAllCategories());
      return "pages/add-product-info";
    }
    productInfoService.saveProductInfo(productInfo);
    return "redirect:" + AppRoutes.LIST_PRODUCT_INFOS;
  }

  @GetMapping(AppRoutes.EDIT_PRODUCT_INFO)
  public String editProductInfoPage(@PathVariable String id, Model model) {
    Optional<ProductInfo> productInfo = productInfoService.getProductInfoById(id);
    if (productInfo.isEmpty()) {
      return "redirect:" + AppRoutes.LIST_PRODUCT_INFOS;
    }
    model.addAttribute("pageTitle", "Edit Product Info");
    model.addAttribute("productInfo", productInfo.get());
    model.addAttribute("categories", categoryService.getAllCategories());
    return "pages/edit-product-info";
  }

  @PostMapping(AppRoutes.EDIT_PRODUCT_INFO_POST)
  public String editProductInfo(@ModelAttribute ProductInfo productInfo, Model model) {
    ProductInfo existingProductInfo = productInfoService.getProductInfoByName(
      productInfo.getName()
    );
    if (existingProductInfo != null && !existingProductInfo.getId().equals(productInfo.getId())) {
      model.addAttribute("errorMessage", "Product name already exists");
      model.addAttribute("productInfo", productInfo);
      model.addAttribute("categories", categoryService.getAllCategories());
      return "pages/edit-product-info";
    }
    // Update the existing product info
    productInfoService.saveProductInfo(productInfo);
    return "redirect:" + AppRoutes.LIST_PRODUCT_INFOS;
  }
}
