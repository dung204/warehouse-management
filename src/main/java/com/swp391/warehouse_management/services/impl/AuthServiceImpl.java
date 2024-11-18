package com.swp391.warehouse_management.services.impl;

import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.services.AuthService;
import java.util.Optional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  @Override
  public Optional<User> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication instanceof AnonymousAuthenticationToken) {
      return Optional.empty();
    }

    return Optional.of((User) authentication.getPrincipal());
  }
}
