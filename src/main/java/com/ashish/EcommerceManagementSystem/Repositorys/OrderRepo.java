package com.ashish.EcommerceManagementSystem.Repositorys;


import com.ashish.EcommerceManagementSystem.Models.Order;
import com.ashish.EcommerceManagementSystem.Models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {

    Order findByUser_IdAndOrderStatus(int userId, OrderStatus orderStatus);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    List<Order> findAllByOrderStatusIn(List<OrderStatus> statuses);

    List<Order> findAllByUser_IdAndOrderStatusNot(int userId, OrderStatus statusToExclude);

    Optional<Order> findByTrackingId(UUID trackingId);

    // convenience if needed
    long count();
}
