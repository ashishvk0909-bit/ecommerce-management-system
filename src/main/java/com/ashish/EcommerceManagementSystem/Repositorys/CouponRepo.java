package com.ashish.EcommerceManagementSystem.Repositorys;

import com.ashish.EcommerceManagementSystem.Models.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepo extends JpaRepository<Coupon, Integer> {

    Optional<Coupon> findByCode(String code);
}
