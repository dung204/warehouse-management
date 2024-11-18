package com.swp391.warehouse_management.configs;

import com.swp391.warehouse_management.common.enums.Role;
import com.swp391.warehouse_management.common.utils.AppRoutes;
import com.swp391.warehouse_management.handlers.CustomAccessDeniedHandler;
import com.swp391.warehouse_management.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserRepository userRepository;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(req -> {
        req.requestMatchers(AppRoutes.publicRoutes.toArray(String[]::new)).permitAll();
        req.requestMatchers(AppRoutes.privateRoutes.toArray(String[]::new)).authenticated();
        req
          .requestMatchers(AppRoutes.stockerRoutes.toArray(String[]::new))
          .hasAuthority(Role.STOCKER.name());
        req
          .requestMatchers(AppRoutes.adminRoutes.toArray(String[]::new))
          .hasAuthority(Role.ADMIN.name());
      })
      .formLogin(config -> config.loginPage(AppRoutes.LOGIN))
      .logout(config -> config.logoutUrl(AppRoutes.LOGOUT))
      .exceptionHandling(config -> config.accessDeniedHandler(customAccessDeniedHandler))
      .build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  UserDetailsService userDetailsService() {
    return username ->
      userRepository
        .findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Username or password is incorrect"));
  }
}
