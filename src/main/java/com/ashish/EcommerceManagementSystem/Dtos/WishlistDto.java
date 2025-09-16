package com.ashish.EcommerceManagementSystem.Dtos;

import lombok.Data;

@Data
public class WishlistDto {
    private int userId;
    private int productId;
    private int id;
    private String productName;
    private String productDescription;
    private byte[] returnImg;
    private int price;
}
