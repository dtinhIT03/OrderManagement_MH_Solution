package com.example.demo.service_jooq;

import generated_sources.Tables;
import generated_sources.tables.pojos.Order;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderJooqService {
    private  final DSLContext dslContext;
    public List<Order> getOrders(){
        return dslContext.selectFrom(Tables.ORDER)
                .fetchInto(Order.class);
    }
}
