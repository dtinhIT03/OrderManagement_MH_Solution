package com.example.demo.repository_jooq;

import com.example.demo.data_jooq.request.OrderRequest;
import com.example.demo.data_jooq.response.OrderResponse;
import generated_sources.tables.pojos.Order;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class JOOQOrderRepository implements OrderRepository{

    private final DSLContext jooq;

    @Override
    public OrderResponse add(OrderRequest orderEntry) {
        return null;
    }

    @Override
    public OrderResponse delete(Long id) {
        return null;
    }

    @Override
    public List<OrderResponse> findAll() {
        return null;
    }

    @Override
    public OrderResponse findById(Long id) {
        return null;
    }

    @Override
    public OrderResponse update(OrderRequest orderEntry) {
        return null;
    }
}
