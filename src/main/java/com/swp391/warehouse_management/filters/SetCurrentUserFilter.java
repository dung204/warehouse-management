package com.swp391.warehouse_management.filters;

import com.swp391.warehouse_management.entities.ExportOrder;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.services.AuthService;
import com.swp391.warehouse_management.services.ExportOrderService;
import com.swp391.warehouse_management.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class SetCurrentUserFilter extends OncePerRequestFilter {

  private final AuthService authService;
  private final UserService userService;
  private final ExportOrderService exportOrderService;

  @Value("${initialAdmin.username}")
  private String initialAdminUsername;

  @Value("${initialAdmin.password}")
  private String initialAdminPassword;

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    Optional<User> currentUser = authService.getCurrentUser();
    User initialAdmin = userService.getUserByUsername(initialAdminUsername);
    List<ExportOrder> pendingOrders = new ArrayList<>();
    if (currentUser.isPresent()) {
      if (currentUser.get().getStock() != null) {
        pendingOrders = exportOrderService.checkExportOrderStatusByStockId(
          currentUser.get().getStock().getId()
        );
      }
      request.setAttribute("currentUser", currentUser.get());
    }
    request.setAttribute("initialAdmin", initialAdmin);
    request.setAttribute("haspendingOrders", (pendingOrders != null && pendingOrders.size() > 0));
    request.setAttribute("pendingOrders", pendingOrders);

    filterChain.doFilter(request, response);
  }
}
