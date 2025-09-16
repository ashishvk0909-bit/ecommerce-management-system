package com.ashish.EcommerceManagementSystem.Controllers;



import com.ashish.EcommerceManagementSystem.Dtos.JwtAuthenticationResponse;
import com.ashish.EcommerceManagementSystem.Dtos.SignInRequest;
import com.ashish.EcommerceManagementSystem.Dtos.SignUpRequest;
import com.ashish.EcommerceManagementSystem.Dtos.UserDto;
import com.ashish.EcommerceManagementSystem.Models.User;
import com.ashish.EcommerceManagementSystem.Models.UserRole;
import com.ashish.EcommerceManagementSystem.Repositorys.UserRepo;
import com.ashish.EcommerceManagementSystem.Services.AuthenticationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepo userTableRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/signup")
    public ResponseEntity<?> SignUpUser(@RequestBody SignUpRequest signUpRequest) {
        UserDto userTableDto = authenticationService.SignUpUser(signUpRequest);
        if (userTableDto == null) {
            return new ResponseEntity<>("User Not Found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userTableDto, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public JwtAuthenticationResponse SignInUser(@RequestBody SignInRequest signInRequest) {
        return authenticationService.SignInUser(signInRequest);
    }


    @PostConstruct
    public void createAdminRole() {
        User userTable1 = userTableRepo.findByRole(UserRole.ADMIN);
        if (userTable1 == null) {
            User userTable = new User();
            userTable.setName("admin");
            userTable.setEmail("admin@gmail.com");
            userTable.setPassword(passwordEncoder.encode("admin"));
            userTable.setRole(UserRole.ADMIN);
            userTableRepo.save(userTable);
        }
    }


}