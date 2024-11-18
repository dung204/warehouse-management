package com.swp391.warehouse_management.controllers;

import com.swp391.warehouse_management.common.utils.AppRoutes;
import com.swp391.warehouse_management.entities.Stock;
import com.swp391.warehouse_management.services.StockService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class StockController {

  private final StockService stockService;

  @GetMapping(AppRoutes.STOCKS)
  public String stockInfo(Model model) {
    model.addAttribute("pageTitle", "List of stocks");
    List<Stock> stocks = stockService.getAllStock();
    model.addAttribute("stocks", stocks);
    return "pages/stocks";
  }

  @GetMapping(AppRoutes.ADD_STOCK)
  public String showAddNewStockPage(Model model) {
    Stock stock = new Stock();
    model.addAttribute("pageTitle", "Add new stock");
    model.addAttribute("stock", stock);
    return "pages/addStocks";
  }

  @PostMapping(AppRoutes.ADD_STOCK)
  public String handleAddNewStock(@ModelAttribute("stock") Stock stock) {
    if (this.stockService.create(stock)) {
      return "redirect:%s".formatted(AppRoutes.STOCKS);
    } else {
      return "redirect:%s".formatted(AppRoutes.ADD_STOCK);
    }
  }

  @GetMapping(AppRoutes.EDIT_STOCK)
  public String showEditStockPage(Model model, @PathVariable("id") String id) {
    Optional<Stock> stock = this.stockService.findByid(id);
    model.addAttribute("pageTitle", "Edit stock");
    model.addAttribute("stock", stock);
    return "pages/editStocks";
  }

  @PostMapping(AppRoutes.EDIT_STOCK)
  public String handleEditStock(@ModelAttribute("stock") Stock stock) {
    if (this.stockService.create(stock)) {
      return "redirect:%s".formatted(AppRoutes.STOCKS);
    } else {
      return "redirect:%s".formatted(AppRoutes.EDIT_STOCK);
    }
  }
}
