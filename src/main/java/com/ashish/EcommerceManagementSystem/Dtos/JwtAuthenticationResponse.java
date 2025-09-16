package com.ashish.EcommerceManagementSystem.Dtos;

import com.ashish.EcommerceManagementSystem.Models.UserRole;
import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String token;
    private UserRole roleTable;
}
