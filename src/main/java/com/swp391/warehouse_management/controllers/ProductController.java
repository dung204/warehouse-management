package com.swp391.warehouse_management.controllers;

import com.swp391.warehouse_management.common.utils.AppRoutes;
import com.swp391.warehouse_management.entities.Product;
import com.swp391.warehouse_management.entities.Stock;
import com.swp391.warehouse_management.entities.StockPosition;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.services.AuthService;
import com.swp391.warehouse_management.services.ProductService;
import com.swp391.warehouse_management.services.StockPositionService;
import com.swp391.warehouse_management.services.StockService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ProductController {

  private final AuthService authService;
  private final ProductService productService;
  private final StockPositionService stockPositionService;
  private final StockService stockService;

  @GetMapping(AppRoutes.UNEXPIRED_PRODUCTS)
  public String unexpiredProductsPage(Model model) {
    User currentUser = authService.getCurrentUser().get();
    String userStockId = currentUser.getStock().getId();

    LocalDate today = LocalDate.now();
    List<Product> unexpiredProducts = productService.getUnexpiredProductsByStockId(userStockId);
    List<StockPosition> stockPositions = stockPositionService.getAllByStockId(userStockId);
    List<Stock> stocks = stockService.getAllStockExceptId(userStockId);
    model.addAttribute("pageTitle", "Unexpired Products");
    model.addAttribute("products", unexpiredProducts);
    model.addAttribute("stockPositions", stockPositions);
    model.addAttribute("stocks", stocks);
    model.addAttribute("today", today);
    return "pages/unexpired-products";
  }

  @PostMapping(AppRoutes.PRODUCTS_CHANGE_POSITION)
  public String changePositionProductsInternal(
    @PathVariable(name = "id") String productId,
    @RequestParam(name = "position") List<String> positions,
    @RequestParam(name = "quantity") List<Integer> quantities,
    RedirectAttributes redirectAttributes,
    HttpServletRequest request
  ) {
    productService.changePositionInternalByProductId(productId, positions, quantities);
    String referer = request.getHeader("Referer");
    redirectAttributes.addFlashAttribute(
      "successMessage",
      "The product have been changed position successfully."
    );
    return "redirect:%s".formatted(referer);
  }

  @GetMapping(AppRoutes.EXPIRED_PRODUCTS)
  public String expiredProductsPage(Model model) {
    User currentUser = authService.getCurrentUser().get();
    List<Product> expiredProducts = productService.getExpiredProductsByStockId(
      currentUser.getStock().getId()
    );
    model.addAttribute("pageTitle", "Expired Products");
    model.addAttribute("expiredProducts", expiredProducts);
    return "pages/expired-products";
  }

  @PostMapping(AppRoutes.EXPIRED_PRODUCTS_DELETE)
  public String deleteExpiredProducts(
    @RequestParam(name = "productIds", required = false) List<String> productIds,
    RedirectAttributes redirectAttributes,
    HttpServletRequest request
  ) {
    if (productIds == null || productIds.isEmpty()) {
      redirectAttributes.addFlashAttribute("errorMessage", "No products selected for deletion.");
      return "redirect:" + AppRoutes.EXPIRED_PRODUCTS;
    }

    productService.deleteProductsByIds(productIds);
    redirectAttributes.addFlashAttribute(
      "successMessage",
      "Selected products have been deleted successfully."
    );
    String referer = request.getHeader("Referer");
    return "redirect:%s".formatted(referer);
  }

  @PostMapping(AppRoutes.PRODUCTS_MOVE_TO_ANOTHER_STOCK)
  public String moveToAnotherStock(
    @RequestParam(name = "stock") String stockId,
    @RequestParam(name = "product-id") List<String> productIds,
    @RequestParam(name = "quantity") List<Integer> quantities,
    RedirectAttributes redirectAttributes
  ) {
    productService.moveProductsToAnotherStock(stockId, productIds, quantities);
    redirectAttributes.addFlashAttribute(
      "successMessage",
      "Selected products have been moved to another stock successfully."
    );
    return "redirect:%s".formatted(AppRoutes.UNEXPIRED_PRODUCTS);
  }

  @GetMapping(AppRoutes.PRODUCTS_FROM_STOCK_POSITION)
  public String showProductsFromStockPosition(
    Model model,
    @PathVariable("id") String stockPositionId
  ) {
    User currentUser = authService.getCurrentUser().get();
    StockPosition stockPosition = stockPositionService
      .findByid(stockPositionId)
      .orElseThrow(() -> new RuntimeException("Stock position not found"));
    List<Product> products = productService.getProductsByStockPositionId(stockPositionId);
    List<StockPosition> stockPositions = stockPositionService.getAllByStockId(
      currentUser.getStock().getId()
    );
    model.addAttribute(
      "pageTitle",
      "Products from Stock Position %s".formatted(stockPosition.getName())
    );
    model.addAttribute("stockPosition", stockPosition);
    model.addAttribute("products", products);
    model.addAttribute("today", LocalDate.now());
    model.addAttribute("stockPositions", stockPositions);
    return "pages/products-from-stock-position";
  }
}
