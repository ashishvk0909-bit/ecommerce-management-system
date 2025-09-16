package com.ashish.EcommerceManagementSystem.Controllers;

import com.ashish.EcommerceManagementSystem.Dtos.AddProductInCartDto;
import com.ashish.EcommerceManagementSystem.Dtos.OrderDto;
import com.ashish.EcommerceManagementSystem.Dtos.PlaceOrderDto;
import com.ashish.EcommerceManagementSystem.Services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller exposing endpoints for customer cart operations:
 * - add product
 * - get current cart by user
 * - apply coupon
 * - increase / decrease product quantity
 * - place order (checkout)
 * - list placed orders
 *
 * Endpoints are under: /api/customer/cart
 */
@RestController
@RequestMapping("/api/customer/cart")
@RequiredArgsConstructor
public class CartController {

    // Service that contains cart business logic
    private final CartService cartService;

    /**
     * Add a product to the user's active/pending cart.
     * Expects AddProductInCartDto { userId, productId } in request body.
     *
     * POST /api/customer/cart/add
     */
    @PostMapping("/add")
    public ResponseEntity<?> addProductToCart(@RequestBody AddProductInCartDto request) {
        // Delegates validation and persistence to CartService
        return cartService.addProductToCart(request);
    }

    /**
     * Get the active (pending) cart for a user.
     * Returns 400 if no active cart found.
     *
     * GET /api/customer/cart/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable int userId) {
        OrderDto cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            // client-friendly message for missing pending cart
            return ResponseEntity.badRequest().body("No active cart found for this user!");
        }
        return ResponseEntity.ok(cart);
    }

    /**
     * Apply coupon to the user's active cart.
     * If coupon invalid or expired, CartService throws RuntimeException and we return 400 with message.
     *
     * POST /api/customer/cart/apply-coupon/{userId}/{couponCode}
     */
    @PostMapping("/apply-coupon/{userId}/{couponCode}")
    public ResponseEntity<?> applyCouponCode(@PathVariable int userId, @PathVariable String couponCode) {
        try {
            OrderDto updatedOrder = cartService.applyCoupon(userId, couponCode);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            // return the error reason to caller (e.g., "Invalid coupon code" or "Coupon expired")
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Increase item quantity (by 1) in the user's active cart.
     * Expects AddProductInCartDto { userId, productId } in body.
     *
     * POST /api/customer/cart/increase
     */
    @PostMapping("/increase")
    public ResponseEntity<?> increaseProductQuantity(@RequestBody AddProductInCartDto request) {
        OrderDto dto = cartService.increaseProductQuantity(request);
        if (dto == null) return ResponseEntity.badRequest().body("Could not increase quantity");
        return ResponseEntity.ok(dto);
    }

    /**
     * Decrease item quantity (by 1) in the user's active cart.
     * If quantity becomes 0 the item is removed.
     *
     * POST /api/customer/cart/decrease
     */
    @PostMapping("/decrease")
    public ResponseEntity<?> decreaseProductQuantity(@RequestBody AddProductInCartDto request) {
        OrderDto dto = cartService.decreaseProductQuantity(request);
        if (dto == null) return ResponseEntity.badRequest().body("Could not decrease quantity");
        return ResponseEntity.ok(dto);
    }

    /**
     * Place (checkout) the active order for the user.
     * Expects PlaceOrderDto { userId, address, orderDescription } in body.
     * On success the pending order becomes Placed and a new empty Pending order is created.
     *
     * POST /api/customer/cart/place
     */
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody PlaceOrderDto request) {
        try {
            OrderDto orderDto = cartService.placeOrder(request);
            return ResponseEntity.ok(orderDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all non-pending (placed/cancelled/etc) orders for the user.
     *
     * GET /api/customer/cart/orders/{userId}
     */
    @GetMapping("/orders/{userId}")
    public ResponseEntity<List<OrderDto>> getMyPlacedOrders(@PathVariable int userId) {
        return ResponseEntity.ok(cartService.getMyPlaceOrders(userId));
    }
}
