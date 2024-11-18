package com.swp391.warehouse_management.controllers;

import com.swp391.warehouse_management.common.utils.AppRoutes;
import com.swp391.warehouse_management.dto.ProductQuantityInStockTODO;
import com.swp391.warehouse_management.entities.ExportOrder;
import com.swp391.warehouse_management.entities.ExportOrderDetails;
import com.swp391.warehouse_management.entities.Product;
import com.swp391.warehouse_management.entities.User;
import com.swp391.warehouse_management.services.AuthService;
import com.swp391.warehouse_management.services.ExportOrderDetailsService;
import com.swp391.warehouse_management.services.ExportOrderService;
import com.swp391.warehouse_management.services.ProductService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ExportProductController {

  private final AuthService authService;
  private final ProductService productService;
  private final ExportOrderService exportOrderService;
  private final ExportOrderDetailsService exportOrderDetailsService;

  @GetMapping(AppRoutes.EXPORT_PRODUCTS)
  public String exportProductsPage(HttpSession session, Model model) {
    User currentUser = authService.getCurrentUser().get();
    List<ProductQuantityInStockTODO> productInventories =
      productService.getAllProductQuantityByStockId(currentUser.getStock().getId());
    String pickerName = (String) session.getAttribute("pickerName");
    @SuppressWarnings("unchecked")
    List<Product> invoiceList = (List<Product>) session.getAttribute("invoiceList");
    model.addAttribute("pageTitle", "Export Products");
    model.addAttribute("products", productInventories);
    model.addAttribute("pickerName", pickerName);
    model.addAttribute("invoiceList", invoiceList);
    return "pages/export-products";
  }

  @PostMapping(AppRoutes.EXPORT_PRODUCTS)
  public String exportProductsProcess(
    @RequestParam(name = "pickerName") String pickerName,
    @RequestParam(name = "product") List<String> productInfoIdList,
    @RequestParam(name = "quantity") List<Integer> quantities,
    Model model,
    HttpSession session,
    RedirectAttributes redirectAttributes
  ) {
    User currentUser = authService.getCurrentUser().get();
    List<Product> invoiceList = productService.getInvoiceListByStockId(
      currentUser.getStock().getId(),
      productInfoIdList,
      quantities
    );
    redirectAttributes.addFlashAttribute("productInfos", productInfoIdList);
    redirectAttributes.addFlashAttribute("quantities", quantities);
    session.setAttribute("pickerName", pickerName);
    session.setAttribute("invoiceList", invoiceList);
    return "redirect:%s".formatted(AppRoutes.EXPORT_PRODUCTS);
  }

  @PostMapping(AppRoutes.CONFIRM_EXPORT_PRODUCTS)
  public String confirmExportProduct(HttpSession session, Model model) {
    String pickerName = (String) session.getAttribute("pickerName");
    @SuppressWarnings("unchecked")
    List<Product> invoiceList = (List<Product>) session.getAttribute("invoiceList");
    productService.updateProductQuantityInStock(invoiceList);
    User currentUser = authService.getCurrentUser().get();
    ExportOrder exportOrder = exportOrderService.createExportOrder(pickerName, currentUser);
    exportOrderDetailsService.addProductsToExportOrderDetail(exportOrder, invoiceList);
    model.addAttribute("products", invoiceList);
    model.addAttribute("exportOrder", exportOrder);
    session.removeAttribute("pickerName");
    session.removeAttribute("invoiceList");
    return "pages/invoice-list-test";
  }

  @GetMapping(AppRoutes.EXPORT_PRODUCT_ORDER_HISTORY)
  public String getExportOrder(Model model) {
    User currentUser = authService.getCurrentUser().get();
    String userStockId = currentUser.getStock().getId();
    List<ExportOrder> exportOrders = exportOrderService.getAllByStockId(userStockId);
    model.addAttribute("pageTitle", "Export orders History");
    model.addAttribute("exportOrders", exportOrders);
    return "pages/export-historys";
  }

  @GetMapping(AppRoutes.EXPORT_PRODUCT_ORDER)
  public String getExportOrderById(Model model, @PathVariable(name = "id") String exportOrderId) {
    ExportOrder exportOrder = exportOrderService.getById(exportOrderId);
    List<ExportOrderDetails> exportOrderDetails = exportOrderDetailsService.getByExportOrderId(
      exportOrderId
    );
    model.addAttribute("pageTitle", "Export Invoice");
    model.addAttribute("exportOrder", exportOrder);
    model.addAttribute("exportOrderDetails", exportOrderDetails);
    return "pages/export-invoice";
  }
}
