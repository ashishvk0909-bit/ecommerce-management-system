package com.ashish.EcommerceManagementSystem.Dtos;

import lombok.Data;

import java.util.Date;

@Data
public class CouponDto {

    private int id;

    private String name;

    private String code;

    private int discount;

    private Date expirationDate;


}
