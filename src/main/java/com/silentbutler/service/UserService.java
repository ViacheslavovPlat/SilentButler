package com.silentbutler.service;

import com.silentbutler.domain.Role;
import com.silentbutler.domain.User;
import com.silentbutler.dto.CreateUserRequest;
import com.silentbutler.dto.UpdateUserRequest;
import com.silentbutler.dto.UpdateMeRequest;
import com.silentbutler.dto.UserResponse;
import com.silentbutler.exception.ResourceAlreadyExistsException;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.RoleRepository;
import com.silentbutler.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.getRole()));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(role)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return mapToUserResponse(user);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToUserResponse(user);
    }

    // Used by /me — resolves the caller from the JWT username
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    // Used by PUT /{id} — can update username, email and role
    public UserResponse updateUser(Long id, UpdateUserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getUsername().equals(userRequest.getUsername())
                && userRepository.existsByUsername(userRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already taken");
        }
        if (!user.getEmail().equals(userRequest.getEmail())
                && userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        Role role = roleRepository.findByName(userRequest.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + userRequest.getRole()));

        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setRole(role);

        return mapToUserResponse(userRepository.save(user));
    }

    // Used by PUT /me — can only update username and email, not role
    public UserResponse updateUserByUsername(String username, UpdateMeRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getUsername().equals(request.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already taken");
        }
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        // intentionally not touching role — users can't promote themselves

        return mapToUserResponse(userRepository.save(user));
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .build();
    }
}