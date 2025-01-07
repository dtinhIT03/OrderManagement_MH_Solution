package com.example.demo.repository_jooq;

import com.example.demo.data_jooq.request.OrderRequest;
import com.example.demo.data_jooq.response.OrderResponse;
import generated_sources.tables.pojos.Order;

import java.util.List;

public interface OrderRepository {
    public OrderResponse add(OrderRequest orderEntry);
    public OrderResponse delete(Long id);
    public List<OrderResponse> findAll();
    public OrderResponse findById(Long id);
    public OrderResponse update(OrderRequest orderEntry);

}
