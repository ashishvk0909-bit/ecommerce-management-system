package com.ashish.EcommerceManagementSystem.Repositorys;


import com.ashish.EcommerceManagementSystem.Models.User;
import com.ashish.EcommerceManagementSystem.Models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    User findByRole(UserRole role);
}
