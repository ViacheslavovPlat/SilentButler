package com.silentbutler.controller;

import com.silentbutler.dto.UpdateUserRequest;
import com.silentbutler.dto.UpdateMeRequest;
import com.silentbutler.dto.UserResponse;
import com.silentbutler.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ── /me endpoints (any authenticated user) ──────────────────────────────

    @GetMapping("/me")
    public UserResponse getMe(Authentication authentication) {
        return userService.getUserByUsername(authentication.getName());
    }

    @PutMapping("/me")
    public UserResponse updateMe(Authentication authentication,
                                 @Valid @RequestBody UpdateMeRequest request) {
        return userService.updateUserByUsername(authentication.getName(), request);
    }

    // ── Admin-only endpoints (guarded by SecurityConfig) ────────────────────

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserResponse editUserById(@PathVariable Long id,
                                     @Valid @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "User deleted successfully";
    }
}