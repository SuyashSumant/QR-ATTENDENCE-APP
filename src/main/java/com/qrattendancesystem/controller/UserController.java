package com.qrattendancesystem.controller;

import com.qrattendancesystem.entity.User;
import com.qrattendancesystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")  // Base URL: http://localhost:8080/api/users
public class UserController {

    @Autowired
    private UserRepository userRepository;  // Injecting Repository to interact with DB

    // 1️⃣ API for User Registration
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // Check if email already exists
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return "Email already exists! Please log in.";
        }

        userRepository.save(user);  // Save user in DB
        return "User registered successfully!";
    }

    // 2️⃣ API for User Login
    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent() && existingUser.get().getPassword().equals(user.getPassword())) {
            return "Login successful!";
        } else {
            return "Invalid email or password!";
        }
    }
}
