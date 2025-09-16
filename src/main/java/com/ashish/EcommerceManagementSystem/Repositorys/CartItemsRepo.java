package com.ashish.EcommerceManagementSystem.Repositorys;

import com.ashish.EcommerceManagementSystem.Models.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemsRepo extends JpaRepository<CartItems, Integer> {

    CartItems findByProduct_IdAndOrder_IdAndUser_Id(int productId, int orderId, int userId);
}
