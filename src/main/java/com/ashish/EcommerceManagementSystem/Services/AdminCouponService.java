package com.ashish.EcommerceManagementSystem.Services;


import com.ashish.EcommerceManagementSystem.Models.Coupon;
import com.ashish.EcommerceManagementSystem.Repositorys.CouponRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCouponService {

    private final CouponRepo couponRepo;

    // Create a new coupon
    public Coupon createCoupon(Coupon coupon) {
        return couponRepo.save(coupon);
    }

    // Get all coupons
    public List<Coupon> getCoupons() {
        return couponRepo.findAll();
    }

}