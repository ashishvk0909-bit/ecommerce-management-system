package com.ashish.EcommerceManagementSystem.Dtos;

import lombok.Data;

import java.util.List;

@Data
public class OrderProductResponseDto {

    public List<ProductDto> productDtoList;

    private int orderAmount;


}
