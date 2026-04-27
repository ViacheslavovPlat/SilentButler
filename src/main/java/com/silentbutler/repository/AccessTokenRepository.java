package com.silentbutler.repository;

import com.silentbutler.domain.AccessToken;
import com.silentbutler.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    Optional<AccessToken> findByTokenHash(String tokenHash);
    List<AccessToken> findByUser(User user);
}