package com.ashish.EcommerceManagementSystem.Services;

import com.ashish.EcommerceManagementSystem.Dtos.WishlistDto;
import com.ashish.EcommerceManagementSystem.Models.Product;
import com.ashish.EcommerceManagementSystem.Models.User;
import com.ashish.EcommerceManagementSystem.Models.Wishlist;
import com.ashish.EcommerceManagementSystem.Repositorys.ProductRepo;
import com.ashish.EcommerceManagementSystem.Repositorys.UserRepo;
import com.ashish.EcommerceManagementSystem.Repositorys.WishlistRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    // constructor-injected repositories
    private final WishlistRepo wishlistRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;

    /**
     * Add a product to user's wishlist.
     * Returns the saved WishlistDto (or null if user/product not found).
     */
    public WishlistDto addProductToWishlist(WishlistDto wishlistDto) {
        Optional<Product> productOpt = productRepo.findById(wishlistDto.getProductId());
        Optional<User> userOpt = userRepo.findById(wishlistDto.getUserId());

        if (productOpt.isPresent() && userOpt.isPresent()) {
            Product product = productOpt.get();
            User user = userOpt.get();
            int userId = user.getId();
            int productId = product.getId();

            // Try to find an existing wishlist entry for this user+product
            Optional<Wishlist> existingOpt = wishlistRepo.findByUser_IdAndProduct_Id(userId, productId);
            if (existingOpt.isPresent()) {
                return existingOpt.get().getWishlistDto();
            }

            // Not present -> create and save
            Wishlist wishlist = new Wishlist();
            wishlist.setProduct(product);
            wishlist.setUser(user);
            Wishlist saved = wishlistRepo.save(wishlist);
            return saved.getWishlistDto();
        }
        return null;
    }

        /**
         * Get wishlist DTOs for a user.
         */
    public List<WishlistDto> getWishlistByUserId(int userId) {
        List<Wishlist> items = wishlistRepo.findAllByUser_Id(userId);
        return items.stream().map(Wishlist::getWishlistDto).collect(Collectors.toList());
    }
}
