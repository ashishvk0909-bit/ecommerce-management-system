package com.ashish.EcommerceManagementSystem.Controllers;


import com.ashish.EcommerceManagementSystem.Dtos.OrderDto;
import com.ashish.EcommerceManagementSystem.Models.OrderStatus;
import com.ashish.EcommerceManagementSystem.Services.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    // GET /api/admin/orders/placed -> list orders with statuses Placed/Shipped/Delivered
    @GetMapping("/placed")
    public ResponseEntity<List<OrderDto>> getAllPlacedOrders() {
        List<OrderDto> orders = adminOrderService.getAllPlacedOrders();
        return ResponseEntity.ok(orders);
    }

    // PATCH /api/admin/orders/{orderId}/status?status=Shipped
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> changeOrderStatus(
            @PathVariable int orderId,
            @RequestParam("status") OrderStatus status) {

        OrderDto dto = adminOrderService.changeOrderStatus(orderId, status);
        return ResponseEntity.ok(dto);
    }


}
