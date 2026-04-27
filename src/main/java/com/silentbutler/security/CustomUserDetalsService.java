package com.silentbutler.security;

import com.silentbutler.domain.User;
import com.silentbutler.exception.ResourceNotFoundException;
import com.silentbutler.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetalsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetalsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
          user.getUsername(),
          user.getPassword(),
          List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}
