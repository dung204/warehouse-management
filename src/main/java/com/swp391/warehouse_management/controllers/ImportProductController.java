package com.swp391.warehouse_management.controllers;

import com.swp391.warehouse_management.common.enums.OrderStatus;
import com.swp391.warehouse_management.common.utils.AppRoutes;
import com.swp391.warehouse_management.entities.ExportOrder;
import com.swp391.warehouse_management.entities.ExportOrderDetails;
import com.swp391.warehouse_management.entities.ImportOrder;
import com.swp391.warehouse_management.entities.ImportOrderDetails;
import com.swp391.warehouse_management.entities.ProductInfo;
import com.swp391.warehouse_management.entities.StockPosition;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.services.AuthService;
import com.swp391.warehouse_management.services.ExportOrderDetailsService;
import com.swp391.warehouse_management.services.ExportOrderService;
import com.swp391.warehouse_management.services.ImportOrderDetailsService;
import com.swp391.warehouse_management.services.ImportOrderService;
import com.swp391.warehouse_management.services.ProductInfoService;
import com.swp391.warehouse_management.services.ProductService;
import com.swp391.warehouse_management.services.StockPositionService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ImportProductController {

  private final AuthService authService;
  private final ProductService productService;
  private final ProductInfoService productInfoService;
  private final StockPositionService positionService;
  private final ImportOrderService importOrderService;
  private final ImportOrderDetailsService importOrderDetailsService;
  private final ExportOrderService exportOrderService;
  private final ExportOrderDetailsService exportOrderDetailsService;

  @GetMapping(AppRoutes.IMPORT_PRODUCT)
  public String importProductPage(Model model) {
    User currentUser = authService.getCurrentUser().get();
    String userStockId = currentUser.getStock().getId();

    List<ProductInfo> productInfos = productInfoService.getAllProductInfos();
    List<StockPosition> positions = positionService.getAllByStockId(userStockId);
    model.addAttribute("productInfos", productInfos);
    model.addAttribute("positions", positions);
    return "pages/import-product";
  }

  @PostMapping(AppRoutes.IMPORT_PRODUCT)
  public String importProductProcess(
    Model model,
    @RequestParam(name = "deliverName") String deliverName,
    @RequestParam(name = "productInfo") List<String> productInfoIds,
    @RequestParam(name = "expiredDate") List<LocalDate> expiredDates,
    @RequestParam(name = "totalQuantity") List<Integer> totalQuantityIntegers,
    @RequestParam(name = "position") List<String> positionIds,
    @RequestParam(name = "positionQuantity") List<Integer> positionQuantities
  ) {
    User currentUser = authService.getCurrentUser().get();
    productService.importProductProcess(
      productInfoIds,
      expiredDates,
      totalQuantityIntegers,
      positionIds,
      positionQuantities
    );
    ImportOrder importOrder = importOrderService.createImportOrder(deliverName, currentUser);
    importOrderDetailsService.createImportOrderDetails(
      importOrder,
      productInfoIds,
      expiredDates,
      totalQuantityIntegers
    );
    return "redirect:" + AppRoutes.IMPORT_PRODUCT_ORDER.replace("{id}", importOrder.getId());
  }

  @GetMapping(AppRoutes.IMPORT_PRODUCT_ANOTHER_STOCK)
  public String importFormAnotherStockPage(
    Model model,
    @PathVariable(name = "id") String exportOrderId
  ) {
    User currentUser = authService.getCurrentUser().get();
    String userStockId = currentUser.getStock().getId();
    List<StockPosition> positions = positionService.getAllByStockId(userStockId);
    ExportOrder exportOrder = exportOrderService.getById(exportOrderId);
    if (exportOrder.getStatus().equals(OrderStatus.CONFIRMED)) {
      return "redirect:" + AppRoutes.IMPORT_PRODUCT_ORDER_HISTORY;
    }
    List<ExportOrderDetails> exportOrderDetails = exportOrderDetailsService.getByExportOrderId(
      exportOrderId
    );
    model.addAttribute("exportOrder", exportOrder);
    model.addAttribute("exportOrderDetails", exportOrderDetails);
    model.addAttribute("positions", positions);
    return "pages/import-form-another-stock";
  }

  @PostMapping(AppRoutes.IMPORT_PRODUCT_ANOTHER_STOCK)
  public String importFormAnotherStockProcess(
    Model model,
    @PathVariable(name = "id") String exportOrderId,
    @RequestParam(name = "productInfo") List<String> productInfoIds,
    @RequestParam(name = "expiredDate") List<LocalDate> expiredDates,
    @RequestParam(name = "totalQuantity") List<Integer> totalQuantityIntegers,
    @RequestParam(name = "position") List<String> positionIds,
    @RequestParam(name = "positionQuantity") List<Integer> positionQuantities
  ) {
    ExportOrder exportOrder = exportOrderService.getById(exportOrderId);
    exportOrderService.changeStatus(exportOrder, OrderStatus.CONFIRMED);
    User currentUser = authService.getCurrentUser().get();
    productService.importProductProcess(
      productInfoIds,
      expiredDates,
      totalQuantityIntegers,
      positionIds,
      positionQuantities
    );
    ImportOrder importOrder = importOrderService.createImportOrder(
      exportOrder.getSourceStock(),
      currentUser
    );
    importOrderDetailsService.createImportOrderDetails(
      importOrder,
      productInfoIds,
      expiredDates,
      totalQuantityIntegers
    );
    return "redirect:" + AppRoutes.IMPORT_PRODUCT_ORDER.replace("{id}", importOrder.getId());
  }

  @GetMapping(AppRoutes.IMPORT_PRODUCT_ORDER)
  public String getImportOrderById(Model model, @PathVariable(name = "id") String importOrderId) {
    ImportOrder importOrder = importOrderService.getById(importOrderId);
    List<ImportOrderDetails> importOrderDetails = importOrderDetailsService.getByimportOrderId(
      importOrderId
    );
    model.addAttribute("pageTitle", "Import Invoice");
    model.addAttribute("importOrder", importOrder);
    model.addAttribute("importOrderDetails", importOrderDetails);
    return "pages/import-invoice";
  }

  @GetMapping(AppRoutes.IMPORT_PRODUCT_ORDER_HISTORY)
  public String getImportOrder(Model model) {
    User currentUser = authService.getCurrentUser().get();
    String userStockId = currentUser.getStock().getId();
    List<ImportOrder> importOrders = importOrderService.getAllByStockId(userStockId);
    model.addAttribute("pageTitle", "Import orders History");
    model.addAttribute("importOrders", importOrders);
    return "pages/import-historys";
  }
}
