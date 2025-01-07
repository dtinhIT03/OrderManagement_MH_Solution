package com.example.demo.data_jooq.request;

import com.example.demo.dto.validator.EnumPattern;
import com.example.demo.entity.OrderProduct;
import com.example.demo.enums.StatusOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class OrderRequest {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "order date must be not null")
    LocalDateTime orderDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime deliveryDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime receiveDate;

    @NotNull(message = "status order mus be not null")
    @EnumPattern(name = "status",regexp = "PENDING|PROCESSING|SHIPPED|DELIVERED|RECIEVED|CANCELED|FAILED|RETURNED")
    StatusOrder statusOrder;

    @NotNull(message = "userId must be not null")
    Long userId;

    @NotNull(message = "items must be not null")
    List<OrderProduct> items;


}
