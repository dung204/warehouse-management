package com.swp391.warehouse_management.services.impl;

import com.swp391.warehouse_management.common.enums.Role;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.repositories.UserRepository;
import com.swp391.warehouse_management.services.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public List<User> getAllUser() {
    return userRepository.findAll();
  }

  @Override
  public User getUserById(String id) {
    return userRepository.findById(id).orElse(null);
  }

  @Override
  public User saveUser(User user) {
    User currUser = user.getId() != null ? getUserById(user.getId()) : null;
    if (currUser != null && (user.getPassword() == null || user.getPassword().isEmpty())) {
      user.setPassword(currUser.getPassword());
    } else {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
    if (user.getRole().equals(Role.ADMIN)) {
      user.setStock(null);
    }
    return userRepository.save(user);
  }

  @Override
  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username).orElse(null);
  }

  @Override
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }
}
