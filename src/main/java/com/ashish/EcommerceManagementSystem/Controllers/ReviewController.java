package com.ashish.EcommerceManagementSystem.Controllers;

import com.ashish.EcommerceManagementSystem.Dtos.OrderProductResponseDto;
import com.ashish.EcommerceManagementSystem.Dtos.ReviewDto;
import com.ashish.EcommerceManagementSystem.Services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for review-related endpoints.
 *
 * Endpoints:
 *  - GET  /api/customer/reviews/order/{orderId}   -> products for an order
 *  - POST /api/customer/reviews                  -> create a review (multipart/form-data)
 *
 * This version does not catch exceptions; they propagate to Spring.
 */
@RestController
@RequestMapping("/api/customer/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Get product details from an order so the frontend can present reviewable products.
     * Example: GET /api/customer/reviews/order/200
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderProductResponseDto> getOrderProductDetailsByOrderID(@PathVariable int orderId) {
        OrderProductResponseDto dto = reviewService.getOrderProductDetailsByOrderID(orderId);
        if (dto == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(dto);
    }

    /**
     * Submit a review. Accepts multipart/form-data.
     * Fields:
     *   - rating (int)
     *   - description (String)
     *   - productId (int)
     *   - userId (int)
     *   - img (file) optional
     *
     * This method declares IOException (propagates if file read fails).
     */
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> giveReview(@ModelAttribute ReviewDto reviewDto) throws java.io.IOException {
        ReviewDto saved = reviewService.giveReview(reviewDto);
        if (saved == null) {
            return ResponseEntity.badRequest().body("Unable to save review. Check productId and userId.");
        }
        return ResponseEntity.ok(saved);
    }
}
