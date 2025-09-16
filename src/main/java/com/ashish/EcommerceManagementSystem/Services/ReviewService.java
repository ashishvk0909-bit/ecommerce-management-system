package com.ashish.EcommerceManagementSystem.Services;

import com.ashish.EcommerceManagementSystem.Dtos.OrderProductResponseDto;
import com.ashish.EcommerceManagementSystem.Dtos.ProductDto;
import com.ashish.EcommerceManagementSystem.Dtos.ReviewDto;
import com.ashish.EcommerceManagementSystem.Models.*;
import com.ashish.EcommerceManagementSystem.Repositorys.OrderRepo;
import com.ashish.EcommerceManagementSystem.Repositorys.ProductRepo;
import com.ashish.EcommerceManagementSystem.Repositorys.ReviewRepo;
import com.ashish.EcommerceManagementSystem.Repositorys.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final ReviewRepo reviewRepo;

    /**
     * Return order basic info + list of products in that order.
     * No try/catch — returns null when order not found.
     */
    public OrderProductResponseDto getOrderProductDetailsByOrderID(int orderId) {
        Optional<Order> orderOpt = orderRepo.findById(orderId);
        if (orderOpt.isEmpty()) {
            return null;
        }

        Order order = orderOpt.get();
        OrderProductResponseDto dto = new OrderProductResponseDto();

        // set the order amount (use totalAmount or amount depending on what you want)
        dto.setOrderAmount(order.getAmount());

        List<ProductDto> productList = new ArrayList<>();
        if (order.getCartItems() != null) {
            for (CartItems cartItem : order.getCartItems()) {
                Product product = cartItem.getProduct();
                if (product == null) continue;

                ProductDto p = new ProductDto();
                p.setId(product.getId());
                p.setName(product.getName());
                p.setPrice(product.getPrice());
//                p.setReturnimage(product.getImage());

                // if ProductDto has a quantity field, you can set it using:
                // p.setQuantity(cartItem.getQuantity());

                productList.add(p);
            }
        }

        // IMPORTANT: your DTO field is named productDtoList, so use the generated setter
        dto.setProductDtoList(productList);
        return dto;
    }


    /**
     * Persist a review. If the MultipartFile is present, read bytes (may throw IOException).
     * This method throws IOException if file read fails (no try/catch here).
     */
    public ReviewDto giveReview(ReviewDto reviewDto) throws IOException {
        Optional<Product> productOpt = productRepo.findById(reviewDto.getProductId());
        Optional<User> userOpt = userRepo.findById(reviewDto.getUserId());

        if (productOpt.isEmpty() || userOpt.isEmpty()) {
            return null;
        }

        Review review = new Review();
        review.setProduct(productOpt.get());
        review.setUser(userOpt.get());
        review.setRating(reviewDto.getRating());
        review.setDescription(reviewDto.getDescription());

        MultipartFile uploaded = reviewDto.getImg();
        if (uploaded != null && !uploaded.isEmpty()) {
            // no try/catch — IOException will propagate
            review.setImg(uploaded.getBytes());
        }

        Review saved = reviewRepo.save(review);
        return saved.getDto();
    }

    /**
     * Helper to fetch all reviews for a product.
     */
    public List<ReviewDto> getReviewsForProduct(int productId) {
        List<Review> reviews = reviewRepo.findAllByProduct_Id(productId);
        List<ReviewDto> out = new ArrayList<>();
        for (Review r : reviews) {
            out.add(r.getDto());
        }
        return out;
    }
}
