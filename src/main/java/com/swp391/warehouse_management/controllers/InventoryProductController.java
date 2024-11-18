package com.swp391.warehouse_management.controllers;

import com.swp391.warehouse_management.common.enums.Role;
import com.swp391.warehouse_management.common.utils.AppRoutes;
import com.swp391.warehouse_management.dto.InventoryProductReportDTO;
import com.swp391.warehouse_management.dto.InventoryReportByProductInfoDTO;
import com.swp391.warehouse_management.entities.ProductInfo;
import com.swp391.warehouse_management.entities.Stock;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.repositories.ProductInfoRepository;
import com.swp391.warehouse_management.services.AuthService;
import com.swp391.warehouse_management.services.InventoryProductService;
import com.swp391.warehouse_management.services.StockService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class InventoryProductController {

  private final InventoryProductService inventoryProductService;
  private final AuthService authService;
  private final ProductInfoRepository productInfoRepository;
  private final StockService stockService;

  @GetMapping(AppRoutes.REPORT_INVENTORY_PRODUCTS)
  public String reportInventoryProductsPage(
    Model model,
    @RequestParam(name = "from-date", required = false) String fromDate,
    @RequestParam(name = "to-date", required = false) String toDate,
    @RequestParam(name = "stock-id", required = false) String stockId
  ) {
    model.addAttribute("pageTitle", "Report Inventory Products");
    User currentUser = authService.getCurrentUser().get();

    if (currentUser.getRole().equals(Role.ADMIN)) {
      List<Stock> stocks = stockService.getAllStocks();
      model.addAttribute("stocks", stocks);

      if (stockId == null || stockId.isEmpty()) {
        return "pages/report-inventory-products";
      }
    }

    if (currentUser.getRole().equals(Role.STOCKER)) {
      stockId = currentUser.getStock().getId();
    }

    if (fromDate == null || toDate == null || fromDate.isEmpty() || toDate.isEmpty()) {
      return "pages/report-inventory-products";
    }

    List<InventoryProductReportDTO> inventoryProductReport =
      inventoryProductService.getInventoryProductReport(
        LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        LocalDate.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        stockId
      );

    model.addAttribute("stockId", stockId);
    model.addAttribute("fromDate", fromDate);
    model.addAttribute("toDate", toDate);
    model.addAttribute("inventoryProductReport", inventoryProductReport);
    return "pages/report-inventory-products";
  }

  @GetMapping(AppRoutes.REPORT_INVENTORY_ONE_PRODUCT_INFO)
  public String reportInventoryOneProductInfo(
    Model model,
    @PathVariable("id") String productInfoId,
    @RequestParam(name = "from-date", required = false) String fromDate,
    @RequestParam(name = "to-date", required = false) String toDate,
    @RequestParam(name = "stock-id", required = false) String stockId
  ) {
    User currentUser = authService.getCurrentUser().get();

    if (
      fromDate == null ||
      toDate == null ||
      fromDate.isEmpty() ||
      toDate.isEmpty() ||
      (currentUser.getRole().equals(Role.ADMIN) && stockId == null)
    ) {
      return "redirect:%s".formatted(AppRoutes.REPORT_INVENTORY_PRODUCTS);
    }

    if (currentUser.getRole().equals(Role.STOCKER)) {
      stockId = currentUser.getStock().getId();
    }

    Optional<ProductInfo> productInfo = productInfoRepository.findById(productInfoId);
    if (productInfo.isEmpty()) {
      return "redirect:%s".formatted(AppRoutes.REPORT_INVENTORY_PRODUCTS);
    }

    Optional<Stock> stock = stockService.findByid(stockId);
    if (stock.isEmpty()) {
      return "redirect:%s".formatted(AppRoutes.REPORT_INVENTORY_PRODUCTS);
    }

    List<InventoryReportByProductInfoDTO> inventoryReportByProductInfo =
      inventoryProductService.getInventoryReportByProductInfo(
        LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        LocalDate.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        stockId,
        productInfoId
      );

    model.addAttribute(
      "pageTitle",
      "Report inventory of %s".formatted(productInfo.get().getName())
    );
    model.addAttribute("stock", stock.get());
    model.addAttribute("productInfo", productInfo.get());
    model.addAttribute("inventoryReportByProductInfo", inventoryReportByProductInfo);
    model.addAttribute("fromDate", fromDate);
    model.addAttribute("toDate", toDate);
    return "pages/report-inventory-one-product-info";
  }
}
