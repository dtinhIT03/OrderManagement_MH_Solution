package com.example.demo.data_jooq.response;

import com.example.demo.enums.StatusOrder;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class OrderResponse {

    Long id;


    LocalDateTime orderDate;


    LocalDateTime deliveryDate;


    LocalDateTime recieveDate;


    StatusOrder statusOrder;


    String fullName;

    String address;

    String phone;

    List<OrderProductResponse> items;
}
