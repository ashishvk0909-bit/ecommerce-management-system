package com.ashish.EcommerceManagementSystem.Controllers;


import com.ashish.EcommerceManagementSystem.Dtos.ProductDto;
import com.ashish.EcommerceManagementSystem.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@ModelAttribute ProductDto productDto) throws IOException {
        ProductDto created = productService.createProduct(productDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> getProductsByName(
            @RequestParam(value = "name", required = false) String name) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.ok(productService.getAllProducts());
        }
        return ResponseEntity.ok(productService.getProductsByName(name));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable int id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        boolean deleted = productService.deleteProduct(productId);
        if (deleted) return ResponseEntity.noContent().build();
        return ResponseEntity.badRequest().body("Product not found");
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable int productId, @ModelAttribute ProductDto productDto) throws IOException {
        ProductDto updated = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(updated);
    }



}
