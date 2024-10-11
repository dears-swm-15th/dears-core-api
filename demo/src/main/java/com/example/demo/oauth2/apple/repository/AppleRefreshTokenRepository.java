package com.example.demo.oauth2.apple.repository;

import com.example.demo.oauth2.apple.domain.AppleRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppleRefreshTokenRepository extends JpaRepository<AppleRefreshToken, Long> {
    Optional<AppleRefreshToken> findByUserId(Long userId);
}