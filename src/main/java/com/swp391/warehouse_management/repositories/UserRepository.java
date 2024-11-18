package com.swp391.warehouse_management.repositories;

import com.swp391.warehouse_management.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);
}
