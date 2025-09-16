package com.ashish.EcommerceManagementSystem.Dtos;

import lombok.Data;

@Data
public class CartItemDto {

    private int id;
    private int price;
    private int quantity;
    private int productId;
    private int orderId;
    private String productName;
//    private byte[] returnimage;
    private int userId;

}
