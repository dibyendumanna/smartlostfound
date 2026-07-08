package com.smartlostfound.backend.controller;

import com.smartlostfound.backend.dto.UserRegistrationRequest;
import com.smartlostfound.backend.entity.User;
import com.smartlostfound.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.smartlostfound.backend.dto.LoginRequest;
import com.smartlostfound.backend.dto.LoginResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody UserRegistrationRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());

        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        String token = userService.loginUser(
                request.getEmail(),
                request.getPassword()
        );

        return new LoginResponse(token, "Login successful");
    }
}