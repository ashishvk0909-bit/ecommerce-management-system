package com.ashish.EcommerceManagementSystem.Controllers;

import com.ashish.EcommerceManagementSystem.Dtos.WishlistDto;
import com.ashish.EcommerceManagementSystem.Services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * WishlistController
 *
 * Endpoints (base path): /api/customer/wishlist
 *  - POST   /api/customer/wishlist         -> add a product to user's wishlist
 *  - GET    /api/customer/wishlist/user/{userId} -> fetch wishlist items for a specific user
 */
@RestController
@RequestMapping("/api/customer/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    /**
     * Add product to wishlist.
     * Expects a WishlistDto in the request body containing at least userId and productId.
     *
     * POST /api/customer/wishlist
     */
    @PostMapping
    public ResponseEntity<?> addProductToWishlist(@RequestBody WishlistDto wishlistDto) {
        WishlistDto saved = wishlistService.addProductToWishlist(wishlistDto);
        if (saved == null) {
            // product or user not found
            return ResponseEntity.badRequest().body("Invalid userId or productId");
        }
        return ResponseEntity.ok(saved);
    }

    /**
     * Get all wishlist entries for a user.
     *
     * GET /api/customer/wishlist/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WishlistDto>> getAllWishlistByUserId(@PathVariable int userId) {
        List<WishlistDto> list = wishlistService.getWishlistByUserId(userId);
        return ResponseEntity.ok(list);
    }
}
