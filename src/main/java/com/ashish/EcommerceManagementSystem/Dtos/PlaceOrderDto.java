package com.ashish.EcommerceManagementSystem.Dtos;

import lombok.Data;

@Data
public class PlaceOrderDto {

    private int userId;
    private String address;
    private String orderDescription;


}
