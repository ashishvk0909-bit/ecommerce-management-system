package com.ashish.EcommerceManagementSystem.Services;

//import com.ashish.ecommerce.Dtos.AnalyticsResponse;

import com.ashish.EcommerceManagementSystem.Dtos.OrderDto;
import com.ashish.EcommerceManagementSystem.Models.Order;
import com.ashish.EcommerceManagementSystem.Models.OrderStatus;
import com.ashish.EcommerceManagementSystem.Repositorys.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrderRepo orderRepo;

    // return orders that are placed/shipped/delivered
    public List<OrderDto> getAllPlacedOrders() {
        List<Order> orders = orderRepo.findAllByOrderStatusIn(
                List.of(OrderStatus.Placed, OrderStatus.Shipped, OrderStatus.Delivered));
        return orders.stream().map(Order::getOrderDto).collect(Collectors.toList());
    }

    // change status
    public OrderDto changeOrderStatus(int orderId, OrderStatus status) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setOrderStatus(status);
        orderRepo.save(order);
        return order.getOrderDto();
    }



}
