package com.swp391.warehouse_management.controllers;

import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.services.AuthService;
import com.swp391.warehouse_management.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

  private final UserService userService;
  private final AuthService authService;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/{id}")
  public String showProfile(@PathVariable String id, Model model) {
    User user = userService.getUserById(id);
    if (user == null) {
      return "redirect:/error";
    }
    model.addAttribute("pageTitle", "Profile");
    model.addAttribute("user", user);

    // Check if the current user matches the profile being viewed
    User currentUser = authService.getCurrentUser().get();
    boolean isCurrentUser = id.equals(currentUser.getId());
    model.addAttribute("isCurrentUser", isCurrentUser);

    return "pages/show-profile";
  }

  @GetMapping("/edit")
  public String editProfilePage(Model model) {
    User currentUser = authService.getCurrentUser().get();
    model.addAttribute("pageTitle", "Edit Profile");
    model.addAttribute("user", currentUser);
    return "pages/edit-profile";
  }

  @PostMapping("/edit")
  public String editProfile(
    @ModelAttribute User user,
    @RequestParam(required = false) String currentPassword,
    @RequestParam(required = false) String newPassword,
    @RequestParam(required = false) String confirmPassword,
    Model model
  ) {
    User currentUser = authService.getCurrentUser().get();
    if (!user.getId().equals(currentUser.getId())) {
      return "redirect:/dashboard";
    }

    user.setCreatedTimestamp(currentUser.getCreatedTimestamp());
    user.setRole(currentUser.getRole());
    user.setStock(currentUser.getStock());

    if (currentPassword != null && !currentPassword.isEmpty()) {
      if (!passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
        model.addAttribute("errorCurrentPassword", true);
        model.addAttribute("user", user);
        return "pages/edit-profile";
      }

      if (!newPassword.equals(confirmPassword)) {
        model.addAttribute("errorNewPassword", true);
        model.addAttribute("user", user);
        return "pages/edit-profile";
      }
    }
    user.setPassword((newPassword != null && !newPassword.isEmpty()) ? newPassword : null);

    try {
      user = userService.saveUser(user);
      currentUser.setFirstName(user.getFirstName());
      currentUser.setLastName(user.getLastName());
      currentUser.setDateOfBirth(user.getDateOfBirth());
      currentUser.setAddress(user.getAddress());
      currentUser.setPassword(user.getPassword());
    } catch (Exception e) {
      model.addAttribute("errorMessage", "Error updating profile: " + e.getMessage());
      model.addAttribute("user", user);
      return "pages/edit-profile";
    }
    return "redirect:/profile/" + user.getId();
  }
}
