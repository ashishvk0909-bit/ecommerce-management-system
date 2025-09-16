package com.ashish.EcommerceManagementSystem.Services;


import com.ashish.EcommerceManagementSystem.Dtos.CategoryDto;
import com.ashish.EcommerceManagementSystem.Models.Category;
import com.ashish.EcommerceManagementSystem.Repositorys.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for category business logic.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepo;

    /**
     * Create a new category from DTO.
     */
    public Category createCategory(CategoryDto categoryDto) {
        Category category = convertDtoToEntity(categoryDto);
        return categoryRepo.save(category);
    }

    /**
     * Fetch all categories from database.
     */
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    /**
     * Helper method to convert DTO to Entity.
     */
    private Category convertDtoToEntity(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }
}
