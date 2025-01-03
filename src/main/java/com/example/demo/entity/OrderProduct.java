package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "order_product")
@Entity
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "total_price")
    BigDecimal totalPrice;

    @Column(name = "quantity")
    Long quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product productId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
//    @JsonBackReference  // Áp dụng @JsonBackReference ở đây để ngừng vòng lặp vô hạn khi chuyển JSON
    Order orderId;
}
