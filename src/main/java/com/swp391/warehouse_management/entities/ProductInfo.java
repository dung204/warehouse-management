package com.swp391.warehouse_management.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "product_infos")
public class ProductInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column
  private String name;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @Column
  private String providerName;

  @Column
  private String unit;
}
