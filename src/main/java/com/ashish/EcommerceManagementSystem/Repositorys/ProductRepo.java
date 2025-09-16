package com.ashish.EcommerceManagementSystem.Repositorys;


import com.ashish.EcommerceManagementSystem.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Product database operations.
 */
@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    List<Product> findAllByNameContaining(String name);
}