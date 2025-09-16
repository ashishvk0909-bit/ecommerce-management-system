package com.ashish.EcommerceManagementSystem.Models;

import com.ashish.EcommerceManagementSystem.Dtos.CartItemDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int price;
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    // ✅ Fixed: Many cart items belong to one order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


    public CartItemDto getCartItemDto() {
        CartItemDto dto = new CartItemDto();
        dto.setId(this.id);
        dto.setPrice(this.price);
        dto.setQuantity(this.quantity);

        if (this.product != null) {
            dto.setProductId(this.product.getId());
            dto.setProductName(this.product.getName());
//            dto.setReturnimage(this.product.getImage()); // assuming image is stored as byte[] in Product
        }

        if (this.order != null) {
            dto.setOrderId(this.order.getId());
        }

        if (this.user != null) {
            dto.setUserId(this.user.getId());
        }

        return dto;
    }


}

