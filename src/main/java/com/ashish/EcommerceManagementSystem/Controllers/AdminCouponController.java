package com.ashish.EcommerceManagementSystem.Controllers;


import com.ashish.EcommerceManagementSystem.Models.Coupon;
import com.ashish.EcommerceManagementSystem.Services.AdminCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/coupons")
@RequiredArgsConstructor
public class AdminCouponController {

    private final AdminCouponService adminCouponService;

    // Create coupon
    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        Coupon savedCoupon = adminCouponService.createCoupon(coupon);
        return ResponseEntity.ok(savedCoupon);
    }

    // Get all coupons
    @GetMapping
    public ResponseEntity<List<Coupon>> getCoupons() {
        List<Coupon> coupons = adminCouponService.getCoupons();
        return ResponseEntity.ok(coupons);
    }


}