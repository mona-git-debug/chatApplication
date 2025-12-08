package com.example.chat.controller;

import com.example.chat.dto.AuthRequest;
import com.example.chat.dto.AuthResponse;
import com.example.chat.model.User;
import com.example.chat.service.UserService;
import com.example.chat.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwt;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest req) {

        if (userService.exists(req.getUsername()))
            throw new RuntimeException("User already exists!");

        User user = userService.register(req.getUsername(), req.getPassword());

        return new AuthResponse(jwt.generate(user.getUsername()), user.getUsername());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {

        User user = userService.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        if (!userService
                .findByUsername(req.getUsername())
                .get()
                .getPassword()
                .startsWith("$2a$") // (BCrypt hash)
        ) {
            throw new RuntimeException("Invalid password");
        }

        return new AuthResponse(jwt.generate(user.getUsername()), user.getUsername());
    }
}
