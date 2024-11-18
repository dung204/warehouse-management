package com.swp391.warehouse_management.entities;

import com.swp391.warehouse_management.common.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "export_orders")
public class ExportOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "export_user_id")
  private User exportUser;

  @Column(nullable = true)
  private String pickerName;

  @ManyToOne
  @JoinColumn(name = "source_stock_id")
  private Stock sourceStock;

  // If the destination stock is null, products will be exported to outside the stocks
  @ManyToOne
  @JoinColumn(name = "destination_stock_id", nullable = true)
  private Stock destinationStock;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Column
  @CreationTimestamp
  private LocalDateTime createdTimestamp;
}
