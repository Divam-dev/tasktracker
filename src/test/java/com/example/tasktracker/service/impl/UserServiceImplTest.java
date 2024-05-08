package com.example.tasktracker.service.impl;

import com.example.tasktracker.dto.UserDto;
import com.example.tasktracker.models.Role;
import com.example.tasktracker.models.User;
import com.example.tasktracker.repository.RoleRepository;
import com.example.tasktracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Vadim");
        userDto.setLastName("Vasilev");
        userDto.setEmail("Vadim.Vasilev@gmail.com");
        userDto.setPassword("password");
        userDto.setRole("student");

        Role role = new Role();
        role.setName("ROLE_STUDENT");
        when(roleRepository.findByName("ROLE_STUDENT")).thenReturn(role);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        userService.saveUser(userDto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("Vadim Vasilev", savedUser.getName());
        assertEquals("Vadim.Vasilev@gmail.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(1, savedUser.getRoles().size());
        assertEquals("ROLE_STUDENT", savedUser.getRoles().get(0).getName());
    }

    @Test
    void testFindByEmail() {
        String email = "Vadim.Vasilev@gmail.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        User foundUser = userService.findByEmail(email);

        assertEquals(user, foundUser);
    }

    @Test
    void testFindById() {
        Long id = 1L;
        User user = new User();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(id);

        assertEquals(user, foundUser);
    }

    @Test
    void testFindAllUsers() {
        List<User> users = Arrays.asList(new User(), new User());

        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.findAllUsers();

        assertEquals(users, foundUsers);
    }

    @Test
    void testDeleteUserById() {
        Long id = 1L;

        userService.deleteUserById(id);

        verify(userRepository, times(1)).deleteById(id);
    }
}