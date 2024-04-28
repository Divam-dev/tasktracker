package com.example.tasktracker.API;

import com.example.tasktracker.dto.UserDto;
import com.example.tasktracker.models.User;
import com.example.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

     //API endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        if (userService.findByEmail(userDto.getEmail()) != null) {
            return ResponseEntity.badRequest().body("User with this email already exists.");
        }

        // Encrypt the password and save the user
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userService.saveUser(userDto);

        return ResponseEntity.ok("User registered successfully.");
    }

    // API endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDto userDto) {
        User existingUser = userService.findByEmail(userDto.getEmail());

        if (existingUser == null || !passwordEncoder.matches(userDto.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid email or password.");
        }

        return ResponseEntity.ok("Login successful.");
    }
}