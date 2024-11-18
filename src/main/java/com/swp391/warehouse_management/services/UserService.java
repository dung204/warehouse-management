package com.swp391.warehouse_management.services;

import com.swp391.warehouse_management.entities.User;
import java.util.List;

public interface UserService {
  List<User> getAllUser();
  User getUserByUsername(String username);
  User saveUser(User user);
  boolean existsByUsername(String username);
  User getUserById(String id);
}
