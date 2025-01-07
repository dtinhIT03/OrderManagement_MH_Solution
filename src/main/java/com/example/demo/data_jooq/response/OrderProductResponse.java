package com.example.demo.data_jooq.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OrderProductResponse {
    private String productName;
    private Long quantity;
    private BigDecimal totalPrice;
}
