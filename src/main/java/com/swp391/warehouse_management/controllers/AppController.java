package com.swp391.warehouse_management.controllers;

import com.swp391.warehouse_management.common.enums.Role;
import com.swp391.warehouse_management.common.utils.AppRoutes;
import com.swp391.warehouse_management.entities.Stock;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.services.AuthService;
import com.swp391.warehouse_management.services.StockService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AppController {

  private final AuthService authService;
  private final StockService stockService;

  @GetMapping(AppRoutes.DEFAULT)
  public String redirectToDefaultPageByRole() {
    Optional<User> currentUser = authService.getCurrentUser();
    if (currentUser.isEmpty()) {
      return "redirect:%s".formatted(AppRoutes.LOGIN);
    }

    List<Stock> stocks = stockService.getAllStock();
    if (currentUser.get().getRole().equals(Role.ADMIN)) {
      return "redirect:%s".formatted(
          AppRoutes.DASHBOARD.replace("{stockId}", stocks.get(0).getId())
        );
    }

    return "redirect:%s".formatted(
        AppRoutes.DASHBOARD.replace("{stockId}", currentUser.get().getStock().getId())
      );
  }

  @GetMapping(AppRoutes.ADMIN)
  public String adminPage(Model model) {
    model.addAttribute("pageTitle", "Admin page");
    return "pages/admin";
  }

  @GetMapping(AppRoutes.STOCKER)
  public String stockerPage(Model model) {
    model.addAttribute("pageTitle", "Stocker page");
    return "pages/stocker";
  }

  @GetMapping(AppRoutes.EXAMPLE)
  public String examplePage(Model model) {
    model.addAttribute("pageTitle", "Example page");
    model.addAttribute(
      "fieldNames",
      List.of("ID", "Username", "First name", "Last name", "Date of birth")
    );
    model.addAttribute(
      "users",
      List.of(
        User.builder()
          .id("1")
          .username("user1")
          .firstName("Dung")
          .lastName("Ho")
          .dateOfBirth(LocalDate.of(2004, 10, 15))
          .build(),
        User.builder()
          .id("2")
          .username("user2")
          .firstName("John")
          .lastName("Doe")
          .dateOfBirth(LocalDate.of(1990, 3, 27))
          .build()
      )
    );
    return "pages/example";
  }
}
