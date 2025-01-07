package com.example.demo.service;

import com.example.demo.data_jooq.request.CriteriaRequest;
import com.example.demo.data_jooq.request.OrderRequest;
import com.example.demo.data_jooq.response.OrderResponse;
import com.example.demo.data_jooq.response.PageResponse;
import com.example.demo.enums.StatusOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService {
    //Get Order By Id
    OrderResponse getOrder(long orderId);

    //Get List Order By Status
    PageResponse<List<OrderResponse>> getOrdersByStatus(StatusOrder status, int pageNo, int pageSize);

    //Get List Order
    PageResponse<List<OrderResponse>> getOrders(int pageNo, int pageSize);

    //Update Order
    void updateOrder(OrderRequest request,long orderId);

    //Create Order
    long saveOrder(OrderRequest request);

    //Delete Order By Id
    void deleteOrderById(long orderId);

    //Update Status Order
    void changeStatusOrder(long orderId,StatusOrder status);

    //Update Delivery Date
    void updateDeliveryDate(long orderId, LocalDateTime deliveryDate);

    //Update Recevice Date
    void updateRecieveDate(long orderId,LocalDateTime recieveDate );

    //Get Orders Advanced
    PageResponse<List<OrderResponse>> getOrdersAdvanced(Map<String,Object> params);

    PageResponse<List<OrderResponse>> getOrdersAdvancedByCrietia(int offset,int pageSize, String... params);

    PageResponse<List<OrderResponse>> getOrdersAdvancedByCrietia_1(int offset, int pageSize, CriteriaRequest params);
}
