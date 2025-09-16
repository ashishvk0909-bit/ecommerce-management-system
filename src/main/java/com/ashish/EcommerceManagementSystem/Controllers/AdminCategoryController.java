package com.ashish.EcommerceManagementSystem.Controllers;


import com.ashish.EcommerceManagementSystem.Dtos.CategoryDto;
import com.ashish.EcommerceManagementSystem.Models.Category;
import com.ashish.EcommerceManagementSystem.Services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    // Create a new category
    @PostMapping
    public Category createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    // Get all categories
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }


}