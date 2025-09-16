package com.ashish.EcommerceManagementSystem.Services;



import com.ashish.EcommerceManagementSystem.Dtos.ProductDto;
import com.ashish.EcommerceManagementSystem.Models.Category;
import com.ashish.EcommerceManagementSystem.Models.Product;
import com.ashish.EcommerceManagementSystem.Repositorys.CategoryRepo;
import com.ashish.EcommerceManagementSystem.Repositorys.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;


    public ProductDto createProduct(ProductDto productDto) throws IOException {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());

        if (productDto.getImageFile() != null && !productDto.getImageFile().isEmpty()) {
            product.setImage(productDto.getImageFile().getBytes());
        }

        Category category = categoryRepo.findById(productDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        Product saved = productRepo.save(product);
        return saved.toDto();
    }

    public List<ProductDto> getAllProducts() {
        return productRepo.findAll().stream().map(Product::toDto).collect(Collectors.toList());
    }

    public List<ProductDto> getProductsByName(String name) {
        return productRepo.findAllByNameContaining(name).stream().map(Product::toDto).collect(Collectors.toList());
    }

    public boolean deleteProduct(int id) {
        Optional<Product> opt = productRepo.findById(id);
        if (opt.isPresent()) {
            productRepo.delete(opt.get());
            return true;
        } else return false;
    }

    public ProductDto getProductById(int id) {
        Product product = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return product.toDto();
    }

    public ProductDto updateProduct(int productId, ProductDto productDto) throws IOException {
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());

        if (productDto.getImageFile() != null && !productDto.getImageFile().isEmpty()) {
            product.setImage(productDto.getImageFile().getBytes());
        }

        if (productDto.getCategoryId() > 0) {
            Category cat = categoryRepo.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(cat);
        }

        return productRepo.save(product).toDto();
    }


}
