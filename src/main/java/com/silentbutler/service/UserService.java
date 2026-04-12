package com.silentbutler.service;

import com.silentbutler.domain.User;
import com.silentbutler.dto.CreateUserRequest;
import com.silentbutler.dto.UpdateUserRequest;
import com.silentbutler.dto.UserResponse;
import com.silentbutler.exception.ResourceAlreadyExistsException;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserResponse createUser(CreateUserRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new ResourceAlreadyExistsException("Username already exists");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .build();
        userRepository.save(user);

        return mapToUserResponse(user);
    }

    public UserResponse getUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent())
            return mapToUserResponse(user.get());
        else
            throw new ResourceNotFoundException("User not found");
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    private UserResponse mapToUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole()).build();
    }

    public void deleteUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User not found")
        );
        userRepository.delete(user);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest userRequest){
        User user = userRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        if(!user.getUsername().equals(userRequest.getUsername())
                && userRepository.existsByUsername(userRequest.getUsername())){
            throw new ResourceAlreadyExistsException("Username already taken");
        }

        if(!user.getEmail().equals(userRequest.getEmail())
                && userRepository.existsByEmail(userRequest.getEmail())){
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setRole(userRequest.getRole());

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }
}
