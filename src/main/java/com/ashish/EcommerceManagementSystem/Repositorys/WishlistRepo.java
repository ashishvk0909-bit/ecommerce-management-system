package com.ashish.EcommerceManagementSystem.Repositorys;

import com.ashish.EcommerceManagementSystem.Models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepo extends JpaRepository<Wishlist, Integer> {
    // find all wishlist entries for a specific user
    List<Wishlist> findAllByUser_Id(int userId);

    // optional convenience: check if wishlist entry exists for user+product
    boolean existsByUser_IdAndProduct_Id(int userId, int productId);
    Optional<Wishlist> findByUser_IdAndProduct_Id(int userId, int productId);

}
