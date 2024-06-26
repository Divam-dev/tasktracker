package com.example.tasktracker.service;

import com.example.tasktracker.dto.UserDto;
import com.example.tasktracker.models.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    User findById(Long id);

    List<User> findAllUsers();

    void deleteUserById(Long id);
}