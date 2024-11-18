package com.swp391.warehouse_management.configs;

import com.swp391.warehouse_management.common.enums.Role;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

  @Value("${initialAdmin.username}")
  private String initialAdminUsername;

  @Value("${initialAdmin.password}")
  private String initialAdminPassword;

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Bean
  CommandLineRunner commandLineRunner() {
    return args -> {
      User initialAdmin = userRepository.findByUsername(initialAdminUsername).orElse(null);

      if (initialAdmin != null) {
        initialAdmin.setPassword(passwordEncoder.encode(initialAdminPassword));
      } else {
        initialAdmin = User.builder()
          .username(initialAdminUsername)
          .password(passwordEncoder.encode(initialAdminPassword))
          .role(Role.ADMIN)
          .build();
      }

      userRepository.save(initialAdmin);
    };
  }
}
