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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "product_info_id")
  private ProductInfo productInfo;

  @ManyToOne
  @JoinColumn(name = "stock_position_id")
  private StockPosition stockPosition;

  @Column
  private LocalDate expiredDate;

  @Column
  private Integer quantity;

  public Product(Product product, int newQuantity) {
    this.id = product.id;
    this.productInfo = product.productInfo;
    this.stockPosition = product.stockPosition;
    this.expiredDate = product.expiredDate;
    this.quantity = newQuantity;
  }
}
