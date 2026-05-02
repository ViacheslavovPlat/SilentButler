package com.silentbutler.service;

import com.silentbutler.domain.AccessToken;
import com.silentbutler.domain.Role;
import com.silentbutler.domain.User;
import com.silentbutler.dto.AuthResponse;
import com.silentbutler.dto.LoginRequest;
import com.silentbutler.dto.RegisterRequest;
import com.silentbutler.exception.InvalidCredentialsException;
import com.silentbutler.exception.ResourceAlreadyExistsException;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.AccessTokenRepository;
import com.silentbutler.repository.RoleRepository;
import com.silentbutler.repository.UserRepository;
import com.silentbutler.security.JWTService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       AccessTokenRepository accessTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JWTService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role USER not found"));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        storeToken(user, token);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!user.isActive()) {
            throw new InvalidCredentialsException("Account is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        storeToken(user, token);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    private void storeToken(User user, String token) {
        AccessToken accessToken = AccessToken.builder()
                .user(user)
                .tokenHash(token)
                .type("BEARER")
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
        accessTokenRepository.save(accessToken);
    }
}
