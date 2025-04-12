package com.karolbystrek.todo.rails.elite.service;

import com.karolbystrek.todo.rails.elite.model.User;
import com.karolbystrek.todo.rails.elite.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "password", "test@email.com", "USER");
        user.setId(1L);
    }

    @Test
    void givenNewUser_whenAddUser_thenReturnSavedUser() {
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";
        user.setPassword(rawPassword);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void givenExistingUsername_whenAddUser_thenThrowException() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userService.addUser(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenExistingEmail_whenAddUser_thenThrowException() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userService.addUser(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenValidUsername_whenGetUserByUsername_thenReturnUser() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByUsername(user.getUsername());

        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    void givenInvalidUsername_whenGetUserByUsername_thenThrowException() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserByUsername(user.getUsername()));
    }

    @Test
    void givenValidEmail_whenGetUserByEmail_thenReturnUser() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByEmail(user.getEmail());

        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void givenInvalidEmail_whenGetUserByEmail_thenThrowException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserByEmail(user.getEmail()));
    }

    @Test
    void givenValidId_whenGetUserById_thenReturnUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(user.getId());

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    void givenInvalidId_whenGetUserById_thenThrowException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(user.getId()));
    }

    @Test
    void givenExistingUser_whenUpdateUser_thenReturnUpdatedUser() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertNotNull(updatedUser);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void givenNonExistingUser_whenUpdateUser_thenThrowException() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenExistingUser_whenDeleteUser_thenUserDeleted() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        userService.deleteUser(user);

        verify(userRepository).delete(user);
    }

    @Test
    void givenNonExistingUser_whenDeleteUser_thenThrowException() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.deleteUser(user));
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void givenUsersExist_whenGetAllUsers_thenReturnUsersList() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.getAllUsers();

        assertFalse(foundUsers.isEmpty());
        assertEquals(1, foundUsers.size());
    }

    @Test
    void givenNoUsers_whenGetAllUsers_thenThrowException() {
        when(userRepository.findAll()).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> userService.getAllUsers());
    }
}
