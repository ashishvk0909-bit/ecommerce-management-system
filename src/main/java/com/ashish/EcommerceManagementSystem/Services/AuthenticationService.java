package com.ashish.EcommerceManagementSystem.Services;


import com.ashish.EcommerceManagementSystem.Dtos.JwtAuthenticationResponse;
import com.ashish.EcommerceManagementSystem.Dtos.SignInRequest;
import com.ashish.EcommerceManagementSystem.Dtos.SignUpRequest;
import com.ashish.EcommerceManagementSystem.Dtos.UserDto;
import com.ashish.EcommerceManagementSystem.Models.Order;
import com.ashish.EcommerceManagementSystem.Models.OrderStatus;
import com.ashish.EcommerceManagementSystem.Models.User;
import com.ashish.EcommerceManagementSystem.Models.UserRole;
import com.ashish.EcommerceManagementSystem.Repositorys.OrderRepo;
import com.ashish.EcommerceManagementSystem.Repositorys.UserRepo;
import com.ashish.EcommerceManagementSystem.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    UserRepo userTableRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    OrderRepo orderRepo;




    public UserDto SignUpUser(SignUpRequest signUpRequest) {
        User userTable = new User();
        userTable.setName(signUpRequest.getName());
        userTable.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userTable.setEmail(signUpRequest.getEmail());
        userTable.setRole(UserRole.CUSTOMER);
        userTableRepo.save(userTable);

        Order order = new Order();
        order.setAmount(0);
        order.setTotalAmount(0);
        order.setDiscount(0);
        order.setUser(userTable);
        order.setOrderStatus(OrderStatus.Pending);
        orderRepo.save(order);



        UserDto userTableDto = new UserDto();
        userTableDto.setName(userTable.getUsername());
        userTableDto.setPassword(userTable.getPassword());
        userTableDto.setEmail(userTable.getEmail());
        userTableDto.setRole(UserRole.CUSTOMER);
        return userTableDto;

    }

    public JwtAuthenticationResponse SignInUser(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
        var user = userTableRepo.findByEmail(signInRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("email not found"));
        var jwt = jwtUtil.generateToken(user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRoleTable(user.getRole());
        return jwtAuthenticationResponse;
    }



}
