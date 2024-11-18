package com.swp391.warehouse_management.controllers;

import com.swp391.warehouse_management.common.enums.Role;
import com.swp391.warehouse_management.common.utils.AppRoutes;
import com.swp391.warehouse_management.entities.Stock;
import com.swp391.warehouse_management.entities.StockPosition;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.services.AuthService;
import com.swp391.warehouse_management.services.ProductService;
import com.swp391.warehouse_management.services.StockPositionService;
import com.swp391.warehouse_management.services.StockService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class StockPositionController {

  private final AuthService authService;
  private final StockPositionService stockPositionService;
  private final StockService stockService;
  private final ProductService productService;

  @GetMapping(AppRoutes.STOCK_POSITIONS_BY_STOCK_ID)
  public String showStockPositionsByStockIdPage(
    Model model,
    @PathVariable("stockId") String stockId
  ) {
    User user = authService.getCurrentUser().get();
    model.addAttribute("pageTitle", "List of Stock Positions");

    if (user.getRole().equals(Role.STOCKER)) {
      if (!user.getStock().getId().equals(stockId)) {
        return "redirect:%s".formatted(
            AppRoutes.STOCK_POSITIONS_BY_STOCK_ID.replace("{stockId}", user.getStock().getId())
          );
      }

      model.addAttribute(
        "stockPositionsForStocker",
        stockPositionService.getAllPositionSummariesByStockId(user.getStock().getId())
      );
      return "pages/stock-positions-for-stocker";
    }

    Stock stock = stockService
      .findByid(stockId)
      .orElseThrow(() -> new RuntimeException("Stock not found"));
    List<StockPosition> stockPositions = stockPositionService.getAllStockPositionByStockId(stockId);
    Map<String, Integer> productQuantities = new HashMap<>();
    for (StockPosition stockPosition : stockPositions) {
      int productCount = productService.getNumberOfProductsByPositionId(stockPosition.getId());
      productQuantities.put(stockPosition.getId(), productCount);
    }

    model.addAttribute("productQuantities", productQuantities);
    model.addAttribute("stock", stock);
    model.addAttribute("listStockPositions", stockPositions);
    return "pages/listStockPositions";
  }

  @GetMapping(AppRoutes.ADD_STOCK_POSITION)
  public String showAddStockPositionPage(Model model, @PathVariable("stockId") String id) {
    Optional<Stock> stock = this.stockService.findByid(id);
    StockPosition stockPosition = new StockPosition();
    model.addAttribute("pageTitle", "Add Stock Position");
    model.addAttribute("stock", stock.get());
    model.addAttribute("stockPosition", stockPosition);
    return "pages/addStockPositions";
  }

  @PostMapping(AppRoutes.ADD_STOCK_POSITION)
  public String handleAddStockPosition(
    @ModelAttribute("stockPosition") StockPosition stockPosition,
    @PathVariable("stockId") String stockId
  ) {
    Stock stock =
      this.stockService.findByid(stockId).orElseThrow(
          () -> new RuntimeException("Stock not found")
        );
    stockPosition.setStock(stock);
    if (this.stockPositionService.create(stockPosition)) {
      return "redirect:/stock-positions/list/" + stockId;
    } else {
      return "redirect:/stock-positions/add/" + stockId;
    }
  }

  @GetMapping(AppRoutes.EDIT_STOCK_POSITION)
  public String showEditStockPosition(Model model, @PathVariable("id") String id) {
    StockPosition stockPosition =
      this.stockPositionService.findByid(id).orElseThrow(
          () -> new RuntimeException("Stock position not found")
        );
    model.addAttribute("pageTitle", "Edit Stock Position");
    model.addAttribute("stockPosition", stockPosition);
    return "pages/editNamePositionStocks";
  }

  @PostMapping(AppRoutes.EDIT_STOCK_POSITION)
  public String handleEditStockPosition(
    @ModelAttribute("stockPosition") StockPosition stockPosition,
    @PathVariable("id") String id
  ) {
    Stock stock = stockPositionService.getStockById(id);
    if (
      this.stockPositionService.updateStockPositionName(
          stockPosition.getId(),
          stockPosition.getName()
        )
    ) {
      return "redirect:/stock-positions/list/" + stock.getId();
    } else {
      return "redirect:/stock-positions/add/" + stock.getId();
    }
  }

  @GetMapping(AppRoutes.REMOVE_STOCK_POSITION)
  public String handleRemoveStockPosition(
    RedirectAttributes redirectAttributes,
    @PathVariable("id") String id
  ) {
    Stock stock = stockPositionService.getStockById(id);
    int numberProduct = productService.getNumberOfProductsByPositionId(id);
    boolean isDelete = false;
    if (numberProduct == 0) {
      stockPositionService.removeStockPosition(id);
      isDelete = true;
    }
    redirectAttributes.addFlashAttribute("isDelete", isDelete);
    return "redirect:/stock-positions/list/" + stock.getId();
  }
}
