package com.library.controller;

import com.library.model.User;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

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

    // Delete a user by ID
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully!";
    }

    // Update user information
    @PutMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            return "User not found!";
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());

        userRepository.save(existingUser);
        return "User updated successfully!";
    }



    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
