package com.ashish.EcommerceManagementSystem.Models;

import com.ashish.EcommerceManagementSystem.Dtos.OrderDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String orderDescription;

    private Date orderDate;

    private int amount;

    private String address;

    private String payment;

    // ✅ Fixed: Enum mapping
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalAmount;

    private int discount;

    private UUID trackingId;

    // ✅ Fixed: Many orders can belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // ✅ Fixed: Many orders can belong to one user
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private Coupon coupon;

    // ✅ Matches CartItems (order is ManyToOne there)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    private List<CartItems> cartItems;

    public OrderDto getOrderDto() {
        OrderDto dto = new OrderDto();
        dto.setId(this.getId());
        dto.setOrderDescription(this.getOrderDescription());
        dto.setOrderDate(this.getOrderDate());
        dto.setAmount(this.getAmount());
        dto.setAddress(this.getAddress());
        dto.setPayment(this.getPayment());
        dto.setOrderStatus(this.getOrderStatus());
        dto.setTotalAmount(this.getTotalAmount());
        dto.setDiscount(this.getDiscount());
        dto.setTrackingId(this.getTrackingId());

        if (this.getUser() != null) {
            dto.setUsername(this.getUser().getName()); // assuming User has "name"
        }

        if (this.getCoupon() != null) {
            dto.setCouponName(this.getCoupon().getName());
        }

        if (this.getCartItems() != null) {
            dto.setCartItems(this.getCartItems()
                    .stream()
                    .map(CartItems::getCartItemDto)
                    .toList());
        }

        return dto;
    }


}
