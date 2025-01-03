package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "product")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "category", nullable = false)
    String category;

    @Column(name = "description",nullable = false)
    String description;

    @Column(name = "import_price", nullable = false )
    BigDecimal importPrice;

    @Column(name = "selling_price", nullable = false )
    BigDecimal sellingPrice;

    @Column(name = "created_at" )
    LocalDateTime createAt;

    @Column(name = "sold_quantity")
    Long soldQuantity;

    @Column(name = "quantity")
    Long quantity;




}
