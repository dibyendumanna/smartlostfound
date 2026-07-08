package com.smartlostfound.backend.service;

import com.smartlostfound.backend.entity.User;
import com.smartlostfound.backend.exception.EmailAlreadyExistsException;
import com.smartlostfound.backend.exception.InvalidCredentialsException;
import com.smartlostfound.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.smartlostfound.backend.security.JwtService;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public User registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");

        return userRepository.save(user);
    }

    public String loginUser(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return jwtService.generateToken(user.getEmail());
    }

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
}