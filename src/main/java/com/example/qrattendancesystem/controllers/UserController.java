package com.example.qrattendancesystem.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/register")
    public String registerUser() {
        return "User registered successfully!";
    }
}

