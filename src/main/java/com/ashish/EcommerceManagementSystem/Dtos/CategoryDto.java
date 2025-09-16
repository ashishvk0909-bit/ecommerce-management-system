package com.ashish.EcommerceManagementSystem.Dtos;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for Category.
 * Helps in transferring category data between API and service layer.
 */
@Data
public class CategoryDto {
    private int id;
    private String name;
    private String description;
}