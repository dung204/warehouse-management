package com.swp391.warehouse_management.controllers;

import com.swp391.warehouse_management.common.utils.AppRoutes;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.services.StockService;
import com.swp391.warehouse_management.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final StockService stockService;

  @GetMapping(AppRoutes.LIST_USERS)
  public String userPage(Model model) {
    model.addAttribute("pageTitle", "List of users");
    model.addAttribute("users", userService.getAllUser());
    return "pages/list-user";
  }

  @GetMapping(AppRoutes.ADD_USER)
  public String addUserPage(Model model) {
    model.addAttribute("pageTitle", "Add New User");
    model.addAttribute("user", new User());
    model.addAttribute("stocks", stockService.getAllStocks());
    return "pages/add-user";
  }

  @PostMapping(AppRoutes.ADD_USER)
  public String addUser(@ModelAttribute User user, Model model) {
    if (userService.existsByUsername(user.getUsername())) {
      model.addAttribute("errorMessage", "Username already exists");
      model.addAttribute("user", user);
      return "pages/add-user";
    }
    userService.saveUser(user);
    return "redirect:" + AppRoutes.LIST_USERS;
  }

  @GetMapping(AppRoutes.EDIT_USER)
  public String editUserPage(@PathVariable String username, Model model) {
    User user = userService.getUserByUsername(username);
    if (user == null) {
      return "redirect:" + AppRoutes.LIST_USERS;
    }
    model.addAttribute("pageTitle", "Edit User");
    model.addAttribute("user", user);
    model.addAttribute("stocks", stockService.getAllStocks());
    return "pages/edit-user";
  }

  @PostMapping(AppRoutes.EDIT_USER_POST)
  public String editUser(@ModelAttribute User user, Model model) {
    User existingUser = userService.getUserByUsername(user.getUsername());
    if (existingUser != null && !existingUser.getId().equals(user.getId())) {
      model.addAttribute("errorMessage", "Username already exists");
      model.addAttribute("user", user);
      return "pages/edit-user";
    }
    userService.saveUser(user);
    return "redirect:" + AppRoutes.LIST_USERS;
  }
}
