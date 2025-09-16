package com.ashish.EcommerceManagementSystem.Dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReviewDto {
    private int id;
    private int rating;
    private String description;
    private MultipartFile img;
    private byte[] returnImg;
    private int productId;
    private int userId;
    private String userName;
}
