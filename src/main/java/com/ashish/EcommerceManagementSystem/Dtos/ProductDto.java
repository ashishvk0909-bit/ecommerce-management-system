package com.ashish.EcommerceManagementSystem.Dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

//

/**
 * DTO (Data Transfer Object) for Product.
 * Used to transfer data between frontend and backend.
 */
@Data
public class ProductDto {

    private int id;
    private String name;
    private int price;
    private String description;

    // For sending image back to client
//    private byte[] returnImage;

    // Category info
    private int categoryId;
    private String categoryName;

    // For uploading image from client
    private MultipartFile imageFile;

    private Long Quantity;



}
