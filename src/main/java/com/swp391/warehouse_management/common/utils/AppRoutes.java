package com.swp391.warehouse_management.common.utils;

import java.util.List;
import org.springframework.util.AntPathMatcher;

public class AppRoutes {

  private static final AntPathMatcher matcher = new AntPathMatcher();

  public static final String DEFAULT = "/";
  public static final String STOCKER = "/stocker";
  public static final String DASHBOARD = "/dashboard/{stockId}";
  public static final String ADMIN = "/admin";

  public static final String LOGIN = "/auth/login";
  public static final String LOGOUT = "/auth/logout";
  public static final String EXAMPLE = "/example";
  public static final String STOCKS = "/stocks";
  public static final String ADD_STOCK = "/stocks/add";
  public static final String ADD_STOCK_POSITION = "/stock-positions/add/{stockId}";
  public static final String EDIT_STOCK = "/stocks/edit/{id}";
  public static final String EDIT_STOCK_POSITION = "/stock-positions/edit/{id}";
  public static final String REMOVE_STOCK_POSITION = "/stock-positions/remove/{id}";
  public static final String STOCK_POSITIONS_BY_STOCK_ID = "/stock-positions/list/{stockId}";

  public static final String LIST_USERS = "/user/list";
  public static final String ADD_USER = "/users/add";
  public static final String SAVE_USER = "/users/save";
  public static final String EDIT_USER = "/users/edit/{username}";
  public static final String EDIT_USER_POST = "/users/edit";

  public static final String EXPIRED_PRODUCTS = "/products/expired";
  public static final String EXPIRED_PRODUCTS_DELETE = "/products/expired/delete";
  public static final String DESTROYED_PRODUCTS_HISTORY = "/products/expired/history";
  public static final String UNEXPIRED_PRODUCTS = "/products/unexpired";

  public static final String SHOW_PROFILE = "/profile/{id}";
  public static final String EDIT_PROFILE = "/profile/edit";
  public static final String LIST_CATEGORIES = "/categories";
  public static final String ADD_CATEGORY = "/categories/add";
  public static final String EDIT_CATEGORY = "/categories/edit/{id}";
  public static final String EDIT_CATEGORY_POST = "/categories/edit";
  public static final String LIST_PRODUCT_INFOS = "/product-infos";
  public static final String ADD_PRODUCT_INFO = "/product-infos/add";
  public static final String EDIT_PRODUCT_INFO = "/product-infos/edit/{id}";
  public static final String EDIT_PRODUCT_INFO_POST = "/product-infos/edit";
  public static final String PRODUCTS_CHANGE_POSITION = "/products/change-position/{id}";
  public static final String PRODUCTS_MOVE_TO_ANOTHER_STOCK = "/products/move-to-stock";
  public static final String EXPORT_PRODUCTS = "/products/export";
  public static final String CONFIRM_EXPORT_PRODUCTS = "/products/export/confirm";
  public static final String IMPORT_PRODUCT = "/products/import";
  public static final String IMPORT_PRODUCT_ANOTHER_STOCK =
    "/products/import-form-another-stock/{id}";
  public static final String IMPORT_PRODUCT_ORDER = "/products/import/order/{id}";
  public static final String IMPORT_PRODUCT_ORDER_HISTORY = "/products/import/history";

  public static final String REPORT_INVENTORY_PRODUCTS = "/products/inventory/report";
  public static final String REPORT_INVENTORY_ONE_PRODUCT_INFO = "/products/inventory/report/{id}";

  public static final String PRODUCTS_FROM_STOCK_POSITION = "/products/from-stock-position/{id}";
  public static final String EXPORT_PRODUCT_ORDER = "/products/export/order/{id}";
  public static final String EXPORT_PRODUCT_ORDER_HISTORY = "/products/export/history";

  public static final String CSS_ROUTES = "/css/**";
  public static final String JS_ROUTES = "/js/**";
  public static final String VENDER_ROUTES = "/vendor/**";
  public static final String IMG_ROUTES = "/img/**";
  public static final String ERROR_ROUTES = "/error/**";
  public static final String FAV_ICON = "/favicon.ico";

  public static final List<String> publicRoutes = List.of(
    LOGIN,
    CSS_ROUTES,
    JS_ROUTES,
    ERROR_ROUTES,
    IMG_ROUTES,
    VENDER_ROUTES,
    EDIT_USER_POST,
    STOCK_POSITIONS_BY_STOCK_ID,
    FAV_ICON
  );

  public static final List<String> privateRoutes = List.of(
    DEFAULT,
    LOGOUT,
    EXAMPLE,
    SHOW_PROFILE,
    EDIT_PROFILE,
    REPORT_INVENTORY_PRODUCTS,
    REPORT_INVENTORY_ONE_PRODUCT_INFO,
    DASHBOARD
  );

  public static final List<String> stockerRoutes = List.of(
    STOCKER,
    UNEXPIRED_PRODUCTS,
    EXPIRED_PRODUCTS,
    EXPORT_PRODUCT_ORDER,
    EXPORT_PRODUCT_ORDER_HISTORY,
    EXPIRED_PRODUCTS_DELETE,
    DESTROYED_PRODUCTS_HISTORY,
    PRODUCTS_FROM_STOCK_POSITION,
    PRODUCTS_CHANGE_POSITION,
    PRODUCTS_MOVE_TO_ANOTHER_STOCK,
    IMPORT_PRODUCT,
    IMPORT_PRODUCT_ORDER,
    IMPORT_PRODUCT_ORDER_HISTORY,
    IMPORT_PRODUCT_ANOTHER_STOCK,
    EXPORT_PRODUCTS,
    CONFIRM_EXPORT_PRODUCTS,
    SHOW_PROFILE,
    EDIT_PROFILE
  );

  public static final List<String> adminRoutes = List.of(
    ADMIN,
    LIST_CATEGORIES,
    ADD_CATEGORY,
    EDIT_CATEGORY,
    EDIT_CATEGORY_POST,
    LIST_PRODUCT_INFOS,
    ADD_PRODUCT_INFO,
    EDIT_PRODUCT_INFO,
    EDIT_PRODUCT_INFO_POST,
    LIST_USERS,
    ADD_USER,
    SAVE_USER,
    EDIT_USER,
    EDIT_USER_POST,
    STOCKS,
    ADD_STOCK,
    EDIT_STOCK,
    ADD_STOCK_POSITION,
    REMOVE_STOCK_POSITION,
    EDIT_STOCK_POSITION
  );

  public static boolean isPublicRoute(String route) {
    return publicRoutes.stream().anyMatch(r -> matcher.match(r, route));
  }

  public static boolean isPrivateRoute(String route) {
    return privateRoutes.stream().anyMatch(r -> matcher.match(r, route));
  }

  public static boolean isStockerRoute(String route) {
    return stockerRoutes.stream().anyMatch(r -> matcher.match(r, route));
  }

  public static boolean isAdminRoute(String route) {
    return adminRoutes.stream().anyMatch(r -> matcher.match(r, route));
  }
}
