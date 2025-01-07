package com.example.demo.data_jooq.mapper;

import com.example.demo.data_jooq.request.OrderRequest;
import com.example.demo.data_jooq.response.OrderResponse;
import generated_sources.tables.pojos.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(OrderRequest request);
    OrderResponse toOrderResponse(Order order);
}
