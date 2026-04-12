package com.silentbutler.controller;

import com.silentbutler.domain.User;
import com.silentbutler.dto.CreateUserRequest;
import com.silentbutler.dto.UpdateUserRequest;
import com.silentbutler.dto.UserResponse;
import com.silentbutler.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest userRequest){
        return this.userService.createUser(userRequest);
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable Long id){
        userService.deleteUserById(id);
        return "User deleteted successfully";
    }

    @PutMapping("/{id}")
    public UserResponse editUserById(@PathVariable Long id,@Valid @RequestBody UpdateUserRequest request){
        return userService.updateUser(id, request);
    }
}
