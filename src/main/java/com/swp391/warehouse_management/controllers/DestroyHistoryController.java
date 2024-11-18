package com.swp391.warehouse_management.controllers;

import com.swp391.warehouse_management.common.utils.AppRoutes;
import com.swp391.warehouse_management.entities.DestroyProductHistory;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.services.AuthService;
import com.swp391.warehouse_management.services.DestroyProductHistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DestroyHistoryController {

  private final DestroyProductHistoryService destroyProductHistoryService;
  private final AuthService authService;

  @GetMapping(AppRoutes.DESTROYED_PRODUCTS_HISTORY)
  public String viewDestroyProductHistory(Model model) {
    User currentUser = authService.getCurrentUser().get();
    List<DestroyProductHistory> historyList =
      destroyProductHistoryService.getDestroyProductHistoryByStockId(
        currentUser.getStock().getId()
      );
    model.addAttribute("pageTitle", "Destroyed Products History");
    model.addAttribute("historyList", historyList);
    return "pages/destroyed-products-history";
  }
}
