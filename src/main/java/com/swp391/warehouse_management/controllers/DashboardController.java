package com.swp391.warehouse_management.controllers;

import com.swp391.warehouse_management.common.enums.Role;
import com.swp391.warehouse_management.common.utils.AppRoutes;
import com.swp391.warehouse_management.dto.CategorySummariesDTO;
import com.swp391.warehouse_management.dto.ProductExpiredStatusDTO;
import com.swp391.warehouse_management.dto.ProductSummariesDTO;
import com.swp391.warehouse_management.dto.StockPositionSummariesDTO;
import com.swp391.warehouse_management.entities.Stock;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.services.AuthService;
import com.swp391.warehouse_management.services.CategoryService;
import com.swp391.warehouse_management.services.DestroyProductHistoryService;
import com.swp391.warehouse_management.services.ExportOrderDetailsService;
import com.swp391.warehouse_management.services.ImportOrderDetailsService;
import com.swp391.warehouse_management.services.ProductInfoService;
import com.swp391.warehouse_management.services.ProductService;
import com.swp391.warehouse_management.services.StockPositionService;
import com.swp391.warehouse_management.services.StockService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class DashboardController {

  private final AuthService authService;
  private final StockPositionService stockPositionService;
  private final CategoryService categoryService;
  private final ProductInfoService productInfoService;
  private final ImportOrderDetailsService importOrderDetailsService;
  private final ExportOrderDetailsService exportOrderDetailsService;
  private final ProductService productService;
  private final DestroyProductHistoryService destroyProductHistoryService;
  private final StockService stockService;

  @GetMapping(AppRoutes.DASHBOARD)
  public String stockerDashboard(@PathVariable("stockId") String id, Model model) {
    model.addAttribute("pageTitle", "Dashboard");
    User currentUser = authService.getCurrentUser().get();
    String stockId = currentUser.getRole().equals(Role.STOCKER)
      ? currentUser.getStock().getId()
      : id;
    List<Stock> stocks = stockService.getAllStock();
    List<StockPositionSummariesDTO> positionSummaries =
      stockPositionService.getPositionSummariesByStockId(stockId, 15);
    List<CategorySummariesDTO> categorySummaries = categoryService.getCategorySummariesByStockId(
      stockId
    );
    List<ProductSummariesDTO> productSummaries = productInfoService.getProductSummariesByStockId(
      stockId
    );
    long ImportProducts = importOrderDetailsService.getAllImportProdutsByStockId(stockId);
    long ExportProducts = exportOrderDetailsService.getAllExportProdutsByStockId(stockId);
    long inventoryProducts = productService.getAllProdutsByStockId(stockId);
    long destroyProducts = destroyProductHistoryService.getAllDestroyProductHistoryByStockId(
      stockId
    );
    ProductExpiredStatusDTO pStatusDTO = productService.getProductExpiredStatusStockId(stockId);
    List<Integer> days = List.of(7, 15, 30, 60, 90, 120);
    List<Integer> nearlyExpiredProducts = new ArrayList<>();
    days.forEach(d -> {
      nearlyExpiredProducts.add(productService.countProductsNearExpiry(d, stockId));
    });
    model.addAttribute("positionSummaries", positionSummaries);
    model.addAttribute("categorySummaries", categorySummaries);
    model.addAttribute("productSummaries", productSummaries);
    model.addAttribute("ImportProducts", ImportProducts);
    model.addAttribute("ExportProducts", ExportProducts);
    model.addAttribute("inventoryProducts", inventoryProducts);
    model.addAttribute("destroyProducts", destroyProducts);
    model.addAttribute("pStatusDTO", pStatusDTO);
    model.addAttribute("days", days);
    model.addAttribute("nearlyExpiredProducts", nearlyExpiredProducts);
    model.addAttribute("stocks", stocks);
    model.addAttribute("stockId", stockId);
    return "pages/dashboard";
  }
}
