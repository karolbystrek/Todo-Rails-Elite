package com.karolbystrek.todo.rails.elite.service;

import com.karolbystrek.todo.rails.elite.exceptions.ResourceAlreadyExistsException;
import com.karolbystrek.todo.rails.elite.exceptions.ResourceNotFoundException;
import com.karolbystrek.todo.rails.elite.model.User;
import com.karolbystrek.todo.rails.elite.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // add a user
    public User addUser(@NotNull(message = "User cannot be null") User user) throws ResourceAlreadyExistsException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistsException("User with username '" + user.getUsername() + "' already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("User with email '" + user.getEmail() + "' already exists");
        }
        user.setRoles("USER");

        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    // get a user by username
    public User getUserByUsername(
            @NotNull(message = "Username cannot be null")
            @NotBlank(message = "Username cannot be blank")
            String username
    ) throws ResourceNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found with username: " + username)
                );
    }

    // get a user by email
    public User getUserByEmail(
            @NotNull(message = "Email cannot be null")
            @NotBlank(message = "Email cannot be blank")
            @Email(message = "Email should be valid")
            String email
    ) throws ResourceNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found with email: " + email)
                );
    }

    // get a user by id
    public User getUserById(
            @NotNull(message = "Id cannot be null")
            Long id
    ) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found with id: " + id)
                );
    }

    // update a user
    public User updateUser(@NotNull(message = "User cannot be null") User user) throws ResourceNotFoundException {
        if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
            throw new ResourceNotFoundException("User not found with username: " + user.getUsername());
        }
        return userRepository.save(user);
    }

    // delete a user
    public void deleteUser(@NotNull(message = "User cannot be null") User user) throws ResourceNotFoundException {
        if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
            throw new ResourceNotFoundException("User not found with username: " + user.getUsername());
        }
        userRepository.delete(user);
    }

    public List<User> getAllUsers() throws ResourceNotFoundException {
        if (userRepository.findAll().isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }
        return userRepository.findAll();
    }
}
