package com.example.chat.service;

import com.example.chat.model.User;
import com.example.chat.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public boolean exists(String username) {
        return repo.existsByUsername(username);
    }

    public User register(String username, String password) {
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(password));
        u.setRole("ROLE_USER");

        return repo.save(u);
    }
}
