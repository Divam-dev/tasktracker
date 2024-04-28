package com.example.tasktracker.service.impl;

import com.example.tasktracker.dto.UserDto;
import com.example.tasktracker.models.Role;
import com.example.tasktracker.models.User;
import com.example.tasktracker.repository.RoleRepository;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Assign a default role if none is provided
        String roleName = "ROLE_STUDENT"; // Default role
        if (userDto.getRole() != null) {
            if (userDto.getRole().equalsIgnoreCase("teacher")) {
                roleName = "ROLE_TEACHER";
            } else if (userDto.getRole().equalsIgnoreCase("student")) {
                roleName = "ROLE_STUDENT";
            }
        }

        Role role = checkRoleExist(roleName);
        user.setRoles(new ArrayList<>(Arrays.asList(role)));

        // Log the user information
        LOGGER.info("Saving user: " + user.getEmail());

        userRepository.save(user); // Save the user to the repository
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        String[] name = user.getName().split(" ");
        userDto.setFirstName(name[0]);
        userDto.setLastName(name[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleExist(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
        return role;
    }
}
