package com.library.controller;

import com.library.model.User;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // Set every user to STUDENT role
        user.setRole(User.Role.STUDENT);

        try {
            userRepository.save(user);
            return "User registered successfully as STUDENT!";
        } catch (Exception e) {
            return "Error registering user: " + e.getMessage();
        }
    }

        @GetMapping("/login")
    public ResponseEntity<User> login(@RequestParam String name, @RequestParam String password) {
        User user = userRepository.findByNameAndPassword(name, password);
        if (user == null) {
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
