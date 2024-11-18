package com.swp391.warehouse_management.services.impl;

import com.swp391.warehouse_management.dto.InventoryProductReportDTO;
import com.swp391.warehouse_management.dto.InventoryReportByProductInfoDTO;
import com.swp391.warehouse_management.entities.DestroyProductHistory;
import com.swp391.warehouse_management.entities.ExportOrderDetails;
import com.swp391.warehouse_management.entities.ImportOrderDetails;
import com.swp391.warehouse_management.entities.InventoryProduct;
import com.swp391.warehouse_management.entities.ProductInfo;
import com.swp391.warehouse_management.repositories.DestroyProductHistoryRepository;
import com.swp391.warehouse_management.repositories.ExportOrderDetailsRepository;
import com.swp391.warehouse_management.repositories.ImportOrderDetailsRepository;
import com.swp391.warehouse_management.repositories.InventoryProductRepository;
import com.swp391.warehouse_management.repositories.ProductInfoRepository;
import com.swp391.warehouse_management.services.InventoryProductService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryProductServiceImpl implements InventoryProductService {

  private final InventoryProductRepository inventoryProductRepository;
  private final ProductInfoRepository productInfoRepository;
  private final ImportOrderDetailsRepository importOrderDetailsRepository;
  private final ExportOrderDetailsRepository exportOrderDetailsRepository;
  private final DestroyProductHistoryRepository destroyProductHistoryRepository;

  @Override
  public List<InventoryProductReportDTO> getInventoryProductReport(
    LocalDate fromDate,
    LocalDate toDate,
    String stockId
  ) {
    List<ProductInfo> productInfos = productInfoRepository.findAll();
    List<InventoryProductReportDTO> result = productInfos
      .stream()
      .map(productInfo -> {
        Optional<InventoryProduct> beginningInventoryProduct =
          inventoryProductRepository.findMostRecentByCreatedDateAndProductInfoIdAndStockId(
            fromDate,
            productInfo.getId(),
            stockId
          );
        Optional<InventoryProduct> endingInventoryProduct =
          inventoryProductRepository.findMostRecentByCreatedDateAndProductInfoIdAndStockId(
            toDate,
            productInfo.getId(),
            stockId
          );
        List<ImportOrderDetails> importOrderDetails =
          importOrderDetailsRepository.findByDestinationStockIdAndProductInfoIdAndCreatedTimestampBetween(
            stockId,
            productInfo.getId(),
            fromDate.atStartOfDay(),
            toDate.atTime(23, 59, 59)
          );
        List<ExportOrderDetails> exportOrderDetails =
          exportOrderDetailsRepository.findBySourceStockIdAndProductInfoIdAndCreatedTimestampBetween(
            stockId,
            productInfo.getId(),
            fromDate.atStartOfDay(),
            toDate.atTime(23, 59, 59)
          );
        List<DestroyProductHistory> destroyProductHistories =
          destroyProductHistoryRepository.findByProductInfoIdAndStockIdAndDeletedTimestampBetween(
            productInfo.getId(),
            stockId,
            fromDate.atStartOfDay(),
            fromDate.atTime(23, 59, 59)
          );

        return InventoryProductReportDTO.builder()
          .productInfo(productInfo)
          .beginningQuantity(
            beginningInventoryProduct.isEmpty() ? 0L : beginningInventoryProduct.get().getQuantity()
          )
          .endingQuantity(
            endingInventoryProduct.isEmpty() ? 0L : endingInventoryProduct.get().getQuantity()
          )
          .importedQuantity(
            importOrderDetails.stream().mapToLong(ImportOrderDetails::getQuantity).sum()
          )
          .exportedQuantity(
            exportOrderDetails.stream().mapToLong(ExportOrderDetails::getQuantity).sum()
          )
          .destroyedQuantity(
            destroyProductHistories.stream().mapToLong(DestroyProductHistory::getQuantity).sum()
          )
          .build();
      })
      .collect(Collectors.toList());

    return result;
  }

  @Override
  public List<InventoryReportByProductInfoDTO> getInventoryReportByProductInfo(
    LocalDate fromDate,
    LocalDate toDate,
    String stockId,
    String productInfoId
  ) {
    Long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate);
    List<LocalDate> dates = Stream.iterate(fromDate, date -> date.plusDays(1))
      .limit(daysBetween + 1)
      .collect(Collectors.toList());

    return dates
      .stream()
      .map(date -> {
        Optional<InventoryProduct> inventoryProduct =
          inventoryProductRepository.findMostRecentByCreatedDateAndProductInfoIdAndStockId(
            date,
            productInfoId,
            stockId
          );
        List<ImportOrderDetails> importOrderDetails =
          importOrderDetailsRepository.findByDestinationStockIdAndProductInfoIdAndCreatedTimestampBetween(
            stockId,
            productInfoId,
            date.atStartOfDay(),
            date.atTime(23, 59, 59)
          );
        List<ExportOrderDetails> exportOrderDetails =
          exportOrderDetailsRepository.findBySourceStockIdAndProductInfoIdAndCreatedTimestampBetween(
            stockId,
            productInfoId,
            date.atStartOfDay(),
            date.atTime(23, 59, 59)
          );
        List<DestroyProductHistory> destroyProductHistories =
          destroyProductHistoryRepository.findByProductInfoIdAndStockIdAndDeletedTimestampBetween(
            productInfoId,
            stockId,
            date.atStartOfDay(),
            date.atTime(23, 59, 59)
          );

        return InventoryReportByProductInfoDTO.builder()
          .date(date)
          .inventoryQuantity(inventoryProduct.isEmpty() ? 0L : inventoryProduct.get().getQuantity())
          .importedQuantity(
            importOrderDetails.stream().mapToLong(ImportOrderDetails::getQuantity).sum()
          )
          .exportedQuantity(
            exportOrderDetails.stream().mapToLong(ExportOrderDetails::getQuantity).sum()
          )
          .destroyedQuantity(
            destroyProductHistories.stream().mapToLong(DestroyProductHistory::getQuantity).sum()
          )
          .build();
      })
      .collect(Collectors.toList());
  }
}
