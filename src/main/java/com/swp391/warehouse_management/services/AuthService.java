package com.swp391.warehouse_management.services;

import com.swp391.warehouse_management.entities.User;
import java.util.Optional;

public interface AuthService {
  Optional<User> getCurrentUser();
}
