package com.ashish.EcommerceManagementSystem.Services;


import com.ashish.EcommerceManagementSystem.Dtos.AddProductInCartDto;
import com.ashish.EcommerceManagementSystem.Dtos.OrderDto;
import com.ashish.EcommerceManagementSystem.Dtos.PlaceOrderDto;
import com.ashish.EcommerceManagementSystem.Models.*;
import com.ashish.EcommerceManagementSystem.Repositorys.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final CartItemsRepo cartItemsRepo;
    private final ProductRepo productRepo;
    private final CouponRepo couponRepo;

    public ResponseEntity<?> addProductToCart(AddProductInCartDto request) {
        Order activeOrder = orderRepo.findByUser_IdAndOrderStatus(request.getUserId(), OrderStatus.Pending);
        if (activeOrder == null) return ResponseEntity.badRequest().body("No active order found for this user!");

        Optional<Product> productOpt = productRepo.findById(request.getProductId());
        Optional<User> userOpt = userRepo.findById(request.getUserId());
        if (productOpt.isEmpty() || userOpt.isEmpty()) return ResponseEntity.badRequest().body("Invalid product or user!");

        Product product = productOpt.get();
        User user = userOpt.get();

        CartItems existing = cartItemsRepo.findByProduct_IdAndOrder_IdAndUser_Id(product.getId(), activeOrder.getId(), user.getId());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + 1);
            existing.setPrice(product.getPrice() * existing.getQuantity());
            cartItemsRepo.save(existing);

            activeOrder.setTotalAmount(activeOrder.getTotalAmount() + product.getPrice());
            activeOrder.setAmount(activeOrder.getAmount() + product.getPrice());
            orderRepo.save(activeOrder);
            return ResponseEntity.ok("Product quantity updated in cart!");
        }

        CartItems newItem = new CartItems();
        newItem.setProduct(product);
        newItem.setUser(user);
        newItem.setOrder(activeOrder);
        newItem.setQuantity(1);
        newItem.setPrice(product.getPrice());
        CartItems saved = cartItemsRepo.save(newItem);

        activeOrder.setTotalAmount(activeOrder.getTotalAmount() + saved.getPrice());
        activeOrder.setAmount(activeOrder.getAmount() + saved.getPrice());
        List<CartItems> items = Optional.ofNullable(activeOrder.getCartItems()).orElse(new ArrayList<>());
        items.add(saved);
        activeOrder.setCartItems(items);
        orderRepo.save(activeOrder);

        return ResponseEntity.ok("Product added to cart successfully!");
    }

    public OrderDto getCartByUserId(int userId) {
        Order activeOrder = orderRepo.findByUser_IdAndOrderStatus(userId, OrderStatus.Pending);
        if (activeOrder == null) return null;
        return activeOrder.getOrderDto();
    }

    public OrderDto applyCoupon(int userId, String code) {
        Order activeOrder = orderRepo.findByUser_IdAndOrderStatus(userId, OrderStatus.Pending);
        if (activeOrder == null) throw new RuntimeException("No active cart found for this user!");

        Coupon coupon = couponRepo.findByCode(code).orElseThrow(() -> new RuntimeException("Invalid coupon code"));
        if (coupon.getExpirationDate().before(new Date())) throw new RuntimeException("Coupon expired");

        int discountPercent = coupon.getDiscount();
        int total = activeOrder.getTotalAmount();
        int discounted = total - (total * discountPercent / 100);

        activeOrder.setCoupon(coupon);
        activeOrder.setDiscount(discountPercent);
        activeOrder.setAmount(discounted);
        orderRepo.save(activeOrder);
        return activeOrder.getOrderDto();
    }

    public OrderDto increaseProductQuantity(AddProductInCartDto request) {
        Order activeOrder = orderRepo.findByUser_IdAndOrderStatus(request.getUserId(), OrderStatus.Pending);
        if (activeOrder == null) return null;

        Optional<Product> productOpt = productRepo.findById(request.getProductId());
        if (productOpt.isEmpty()) return null;

        CartItems item = cartItemsRepo.findByProduct_IdAndOrder_IdAndUser_Id(request.getProductId(), activeOrder.getId(), request.getUserId());
        if (item == null) return null;

        item.setQuantity(item.getQuantity() + 1);
        item.setPrice(item.getProduct().getPrice() * item.getQuantity());
        cartItemsRepo.save(item);

        activeOrder.setTotalAmount(activeOrder.getTotalAmount() + item.getProduct().getPrice());
        // Recalculate amount with coupon if present
        if (activeOrder.getCoupon() != null) {
            int discount = activeOrder.getCoupon().getDiscount();
            int total = activeOrder.getTotalAmount();
            activeOrder.setAmount(total - (total * discount / 100));
        } else {
            activeOrder.setAmount(activeOrder.getTotalAmount());
        }
        orderRepo.save(activeOrder);

        return activeOrder.getOrderDto();
    }

    public OrderDto decreaseProductQuantity(AddProductInCartDto request) {
        Order activeOrder = orderRepo.findByUser_IdAndOrderStatus(request.getUserId(), OrderStatus.Pending);
        if (activeOrder == null) return null;

        Optional<Product> productOpt = productRepo.findById(request.getProductId());
        if (productOpt.isEmpty()) return null;

        CartItems item = cartItemsRepo.findByProduct_IdAndOrder_IdAndUser_Id(request.getProductId(), activeOrder.getId(), request.getUserId());
        if (item == null) return null;

        if (item.getQuantity() <= 1) {
            // remove item
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() - item.getPrice());
            activeOrder.getCartItems().remove(item);
            cartItemsRepo.delete(item);
        } else {
            item.setQuantity(item.getQuantity() - 1);
            item.setPrice(item.getProduct().getPrice() * item.getQuantity());
            cartItemsRepo.save(item);
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() - item.getProduct().getPrice());
        }

        if (activeOrder.getCoupon() != null) {
            int discount = activeOrder.getCoupon().getDiscount();
            int total = activeOrder.getTotalAmount();
            activeOrder.setAmount(total - (total * discount / 100));
        } else {
            activeOrder.setAmount(activeOrder.getTotalAmount());
        }

        orderRepo.save(activeOrder);
        return activeOrder.getOrderDto();
    }

    public OrderDto placeOrder(PlaceOrderDto dto) {
        Order activeOrder = orderRepo.findByUser_IdAndOrderStatus(dto.getUserId(), OrderStatus.Pending);
        if (activeOrder == null) throw new RuntimeException("No active cart to place");

        activeOrder.setAddress(dto.getAddress());
        activeOrder.setOrderDescription(dto.getOrderDescription());
        activeOrder.setOrderDate(new Date());
        activeOrder.setOrderStatus(OrderStatus.Placed);
        activeOrder.setTrackingId(UUID.randomUUID());

        // When moving to placed, persist and create new empty pending order
        orderRepo.save(activeOrder);

        Order newPending = new Order();
        newPending.setUser(activeOrder.getUser());
        newPending.setAmount(0);
        newPending.setTotalAmount(0);
        newPending.setDiscount(0);
        newPending.setOrderStatus(OrderStatus.Pending);
        newPending.setOrderDate(new Date());
        orderRepo.save(newPending);

        return activeOrder.getOrderDto();
    }

    public List<OrderDto> getMyPlaceOrders(Integer userId) {
        List<Order> placedOrders = orderRepo.findAllByUser_IdAndOrderStatusNot(userId, OrderStatus.Pending);
        return placedOrders.stream().map(Order::getOrderDto).toList();
    }

    public OrderDto searchOrderByTrackingNumber(UUID trackingNumber) {
        Optional<Order> opt = orderRepo.findByTrackingId(trackingNumber);
        return opt.map(Order::getOrderDto).orElse(null);
    }
}
