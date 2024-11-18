package com.swp391.warehouse_management.services.impl;

import com.swp391.warehouse_management.entities.ExportOrder;
import com.swp391.warehouse_management.entities.ExportOrderDetails;
import com.swp391.warehouse_management.entities.Product;
import com.swp391.warehouse_management.repositories.ExportOrderDetailsRepository;
import com.swp391.warehouse_management.services.ExportOrderDetailsService;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExportOrderDetailsServiceImpl implements ExportOrderDetailsService {

  private final ExportOrderDetailsRepository exportOrderDetailsRepository;

  @Override
  public void addProductsToExportOrderDetail(ExportOrder exportOrder, List<Product> invoiceList) {
    List<ExportOrderDetails> exportOrderDetails = new ArrayList<>();
    invoiceList.forEach(product -> {
      exportOrderDetails.add(
        ExportOrderDetails.builder()
          .exportOrder(exportOrder)
          .productInfo(product.getProductInfo())
          .expiredDate(product.getExpiredDate())
          .quantity(product.getQuantity())
          .build()
      );
    });
    exportOrderDetailsRepository.saveAll(exportOrderDetails);
  }

  @Override
  public List<ExportOrderDetails> getByExportOrderId(String id) {
    return exportOrderDetailsRepository.findAllByExportOrderId(id);
  }

  @Override
  public Long getAllExportProdutsByStockId(String stockId) {
    return exportOrderDetailsRepository.getAllExportProdutsByStockId(stockId);
  }
}
