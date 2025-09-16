package com.ashish.EcommerceManagementSystem.Models;

import com.ashish.EcommerceManagementSystem.Dtos.ReviewDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int rating;

    @Lob
    private String description;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] img;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    /**
     * Convert entity -> DTO for API responses.
     *
     * NOTE: I use user.getUsername() to populate userName. If your User entity uses a
     * different getter (e.g., getName() or getFullName()), replace getUsername() accordingly.
     */
    public ReviewDto getDto() {
        ReviewDto dto = new ReviewDto();
        dto.setId(this.id);
        dto.setRating(this.rating);
        dto.setDescription(this.description);
        dto.setReturnImg(this.img);

        if (this.product != null) {
            dto.setProductId(this.product.getId());
        }
        if (this.user != null) {
            dto.setUserId(this.user.getId());
            dto.setUserName(this.user.getUsername()); // adjust if your getter differs
        }

        return dto;
    }
}
