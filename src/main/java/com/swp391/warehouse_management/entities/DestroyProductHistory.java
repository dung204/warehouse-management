package com.swp391.warehouse_management.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "destroy_products_history")
public class DestroyProductHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "product_info_id")
  private ProductInfo productInfo;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column
  private LocalDate expiredDate;

  @Column
  private Integer quantity;

  @ManyToOne
  @JoinColumn(name = "stock_id")
  private Stock stock;

  @Column
  @CreationTimestamp
  private LocalDateTime deletedTimestamp;
}
