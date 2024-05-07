package com.example.tasktracker.controller.API;

import com.example.tasktracker.dto.UserDto;
import com.example.tasktracker.models.User;
import com.example.tasktracker.requests.LoginRequest;
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

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userService.saveUser(userDto);

        return ResponseEntity.ok("User registered successfully.");
    }

    // API endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.email();
        String password = loginRequest.password();

        User existingUser = userService.findByEmail(email);

        if (existingUser == null || !passwordEncoder.matches(password, existingUser.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid email or password.");
        }

        return ResponseEntity.ok("Login successful.");
    }
}