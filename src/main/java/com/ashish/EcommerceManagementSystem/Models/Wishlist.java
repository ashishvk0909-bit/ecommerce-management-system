package com.ashish.EcommerceManagementSystem.Models;

import com.ashish.EcommerceManagementSystem.Dtos.WishlistDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    /**
     * Convert entity to DTO.
     * Make sure Product and User are loaded before calling (or use DTO mapping in the service).
     */
    public WishlistDto getWishlistDto() {
        WishlistDto wishlistDto = new WishlistDto();
        wishlistDto.setId(this.id);
        if (product != null) {
            wishlistDto.setProductId(product.getId());
            wishlistDto.setProductName(product.getName());
            wishlistDto.setProductDescription(product.getDescription());
            wishlistDto.setPrice(product.getPrice());
            wishlistDto.setReturnImg(product.getImage()); // if product.getImage() -> byte[]
        }
        if (user != null) {
            wishlistDto.setUserId(user.getId());
        }
        return wishlistDto;
    }
}
