package com.ashish.EcommerceManagementSystem.Models;

import com.ashish.EcommerceManagementSystem.Dtos.ProductDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int price;
    private String description;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;


    /**
     * Convert Entity → DTO
     */
    public ProductDto toDto() {
        ProductDto dto = new ProductDto();
        dto.setId(id);
        dto.setName(name);
        dto.setPrice(price);
        dto.setDescription(description);
//        dto.setReturnImage(image);
        dto.setCategoryName(category.getName());
        dto.setCategoryId(category.getId());
        return dto;
    }


}
