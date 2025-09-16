package com.ashish.EcommerceManagementSystem.Dtos;

import com.ashish.EcommerceManagementSystem.Models.OrderStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {

    private int id;

    private String orderDescription;

    private Date orderDate;

    private int amount;

    private String address;

    private String payment;

    private OrderStatus orderStatus;

    private int totalAmount;

    private int discount;

    private UUID trackingId;

    private String username;

    private List<CartItemDto> cartItems;

    private String couponName;
}
