package com.example.demo.service.impl;

import com.example.demo.data_jooq.request.CriteriaRequest;
import com.example.demo.data_jooq.request.OrderRequest;
import com.example.demo.data_jooq.response.OrderProductResponse;
import com.example.demo.data_jooq.response.OrderResponse;
import com.example.demo.data_jooq.response.PageResponse;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderProduct;
import com.example.demo.entity.User;
import com.example.demo.enums.StatusOrder;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.OrderProductRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.SearchRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;
import com.example.demo.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final UserRepository userRepository;
    private final SearchRepository searchRepository;
    @Override
    public OrderResponse getOrder(long orderId) {
        Order order = getOrderById(orderId);
        List<OrderProductResponse> orderProductResponses =  order.getItems().stream().map(orderProduct -> OrderProductResponse.builder()
                .productName(orderProduct.getProductId().getName())
                .quantity(orderProduct.getQuantity())
                .totalPrice(orderProduct.getTotalPrice())
                .build()).toList();
        return OrderResponse.builder()
                .id(orderId)
                .orderDate(order.getOrderDate())
                .deliveryDate(order.getDeliveryDate())
                .recieveDate(order.getRecieveDate())
                .statusOrder(order.getStatusOrder())
                .fullName(order.getUser_id().getFullName())
                .address(order.getUser_id().getAddress())
                .items(orderProductResponses)
                .build();
    }
    private Order getOrderById(long orderId){
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
    }

    private User getUserById(long userId){
        return userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found!"));
    }


    @Override
    public PageResponse<List<OrderResponse>> getOrdersByStatus(StatusOrder status,int pageNo,int pageSize) {

        long totalElements = orderRepository.countByStatusOrder(status);
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        int offset = pageNo * pageSize;
        List<Order> orders = orderRepository.findAllByStatusOrder(String.valueOf(status),pageSize,offset);


        List<OrderResponse> orderResponses = orders.stream().map(order -> {
            List<OrderProductResponse> orderProductResponses = order.getItems().stream().map(orderProduct -> OrderProductResponse.builder()
                    .totalPrice(orderProduct.getTotalPrice())
                    .productName(orderProduct.getProductId().getName())
                    .quantity(orderProduct.getQuantity()).build()).toList();
            return OrderResponse.builder()
                    .id(order.getId())
                    .orderDate(order.getOrderDate())
                    .deliveryDate(order.getDeliveryDate())
                    .recieveDate(order.getRecieveDate())
                    .statusOrder(order.getStatusOrder())
                    .phone(order.getUser_id().getPhone())
                    .address(order.getUser_id().getAddress())
                    .fullName(order.getUser_id().getFullName())
                    .items(orderProductResponses)
                    .build();}).toList();


        return PageResponse.<List<OrderResponse>>builder().totalPages(totalPages)
                .totalElement(totalElements)
                .pageSize(pageSize)
                .pageNo(pageNo)
                .data(orderResponses)
                .build();
    }

    @Override
    public PageResponse<List<OrderResponse>> getOrders(int pageNo, int pageSize) {
        long totalElements = orderRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        int offset = pageNo * pageSize;
        List<Order> orders = orderRepository.findAll(pageSize,offset);

        List<OrderResponse> orderResponses = orders.stream().map(order -> {
            List<OrderProductResponse> orderProductResponses = order.getItems().stream().map(orderProduct -> OrderProductResponse.builder()
                    .totalPrice(orderProduct.getTotalPrice())
                    .productName(orderProduct.getProductId().getName())
                    .quantity(orderProduct.getQuantity()).build()).toList();
            return OrderResponse.builder()
                    .id(order.getId())
                    .orderDate(order.getOrderDate())
                    .deliveryDate(order.getDeliveryDate())
                    .recieveDate(order.getRecieveDate())
                    .statusOrder(order.getStatusOrder())
                    .phone(order.getUser_id().getPhone())
                    .address(order.getUser_id().getAddress())
                    .fullName(order.getUser_id().getFullName())
                    .items(orderProductResponses)
                    .build();}).toList();

        return PageResponse.<List<OrderResponse>>builder().totalPages(totalPages)
                .totalElement(totalElements)
                .pageSize(pageSize)
                .pageNo(pageNo)
                .data(orderResponses)
                .build();
    }

    @Override
    public void updateOrder(OrderRequest request, long orderId) {
        Order order =  getOrderById(orderId);
        order.setOrderDate(request.getOrderDate());
        order.setDeliveryDate(request.getDeliveryDate());
        order.setRecieveDate(request.getReceiveDate());
        order.setStatusOrder(request.getStatusOrder());
        order.setItems(request.getItems());
        orderRepository.save(order);

    }

    @Override
    public long saveOrder(OrderRequest request) {
        User user = getUserById(request.getUserId());
        Order order = Order.builder().orderDate(request.getOrderDate())
                .deliveryDate(request.getDeliveryDate())
                .recieveDate(request.getDeliveryDate())
                .statusOrder(request.getStatusOrder())
                .user_id(user)
                .build();
        orderRepository.save(order);
        for(OrderProduct item : request.getItems()){
            item.setOrderId(order);
        }

        orderProductRepository.saveAll(request.getItems());
        return order.getId();
    }

    @Override
    public void deleteOrderById(long orderId) {
            getOrderById(orderId);
            orderRepository.deleteById(orderId);

    }

    @Override
    public void changeStatusOrder(long orderId, StatusOrder status) {
        Order order = getOrderById(orderId);
        order.setStatusOrder(status);
        orderRepository.save(order);
    }

    @Override
    public void updateDeliveryDate(long orderId, LocalDateTime deliveryDate) {
        Order order = getOrderById(orderId);
        order.setDeliveryDate(deliveryDate);
        orderRepository.save(order);
    }

    @Override
    public void updateRecieveDate(long orderId, LocalDateTime recieveDate) {
        Order order = getOrderById(orderId);
        order.setRecieveDate(recieveDate);
        orderRepository.save(order);
    }

    @Override
    public PageResponse<List<OrderResponse>> getOrdersAdvanced(Map<String, Object> params) {

            String status =params.get("status") != null ? String.valueOf(params.get("status")) : null;
            Long userId = params.get("userId") != null ? Long.parseLong(params.get("userId").toString()) : null;
            String orderDate = params.get("orderDate") != null ? String.valueOf( params.get("orderDate")) : null;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);
            int pageNo = params.get("pageNo") != null  ? Integer.parseInt(params.get("pageNo").toString()) : Integer.parseInt(AppConstants.DEFAULT_PAGE_NUMBER);
            int offset = pageNo * pageSize;
            List<Order> orders = orderRepository.findAllAdvanced(status,userId,orderDate,pageSize,offset);
            int totalElements = orderRepository.countAdvanced(status,userId,orderDate);
            int totalPages = (int) Math.ceil((double) totalElements / pageSize);


        List<OrderResponse> orderResponses = orders.stream().map(order -> {
            List<OrderProductResponse> orderProductResponses = order.getItems().stream().map(orderProduct -> OrderProductResponse.builder()
                    .totalPrice(orderProduct.getTotalPrice())
                    .productName(orderProduct.getProductId().getName())
                    .quantity(orderProduct.getQuantity()).build()).toList();
            return OrderResponse.builder()
                    .id(order.getId())
                    .orderDate(order.getOrderDate())
                    .deliveryDate(order.getDeliveryDate())
                    .recieveDate(order.getRecieveDate())
                    .statusOrder(order.getStatusOrder())
                    .phone(order.getUser_id().getPhone())
                    .address(order.getUser_id().getAddress())
                    .fullName(order.getUser_id().getFullName())
                    .items(orderProductResponses)
                    .build();}).toList();

        return PageResponse.<List<OrderResponse>>builder().totalPages(totalPages)
                .totalElement(totalElements)
                .pageSize(pageSize)
                .pageNo(pageNo)
                .data(orderResponses)
                .build();
    }

    @Override
    public PageResponse<List<OrderResponse>> getOrdersAdvancedByCrietia(int offset,int pageSize,String... params) {
        return searchRepository.advanceSearch(offset,pageSize,params);
    }

    @Override
    public PageResponse<List<OrderResponse>> getOrdersAdvancedByCrietia_1(int offset,int pageSize, CriteriaRequest params) {
        System.out.println(params);
        return searchRepository.advanceSearch_body(offset,pageSize,params);

    }
}
