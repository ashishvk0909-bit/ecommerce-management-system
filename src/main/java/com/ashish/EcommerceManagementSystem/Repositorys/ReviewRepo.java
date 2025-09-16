package com.ashish.EcommerceManagementSystem.Repositorys;

import com.ashish.EcommerceManagementSystem.Models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Integer> {
    // fetch all reviews for a product
    List<Review> findAllByProduct_Id(int productId);
}
