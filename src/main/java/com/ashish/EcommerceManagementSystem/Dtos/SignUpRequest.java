package com.ashish.EcommerceManagementSystem.Dtos;

import com.ashish.EcommerceManagementSystem.Models.UserRole;
import lombok.Data;

@Data
public class SignUpRequest {

    private String name;
    private String email;
    private String password;
    private UserRole roleTable;

}
