package com.ashish.EcommerceManagementSystem.Repositorys;

import com.ashish.EcommerceManagementSystem.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
