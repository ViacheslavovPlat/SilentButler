package com.silentbutler.service;

import com.silentbutler.domain.User;
import com.silentbutler.dto.AuthResponse;
import com.silentbutler.dto.LoginRequest;
import com.silentbutler.dto.RegisterRequest;
import com.silentbutler.exception.InvalidCredentialsException;
import com.silentbutler.exception.ResourceAlreadyExistsException;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.UserRepository;
import com.silentbutler.security.JWTService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JWTService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new ResourceAlreadyExistsException("Username already exists");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPasswrod()))
                .role("USER")
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        boolean passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!passwordMatch){
            throw new InvalidCredentialsException("Invalid username or passwrod");
        }

        String token = jwtService.generateToken(user.getUsername());
        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
