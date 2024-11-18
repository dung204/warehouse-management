package com.swp391.warehouse_management.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "import_orders")
public class ImportOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "import_user_id")
  private User importUser;

  // If products are imported (moved) from another stock, then deliverName = null
  @Column(nullable = true)
  private String deliverName;

  // If products are imported from outside the stocks, then sourceStock = null
  @ManyToOne
  @JoinColumn(name = "source_stock_id", nullable = true)
  private Stock sourceStock;

  @ManyToOne
  @JoinColumn(name = "destination_stock_id")
  private Stock destinationStock;

  @Column
  @CreationTimestamp
  private LocalDateTime createdTimestamp;
}
