package com.swp391.warehouse_management.controllers;

import com.swp391.warehouse_management.common.utils.AppRoutes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

  @GetMapping(AppRoutes.LOGIN)
  public String loginPage(Model model) {
    model.addAttribute("pageTitle", "Login");
    return "pages/login";
  }
}
