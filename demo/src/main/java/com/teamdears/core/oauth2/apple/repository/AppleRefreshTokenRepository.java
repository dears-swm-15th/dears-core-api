package com.teamdears.core.oauth2.apple.repository;

import com.teamdears.core.oauth2.apple.domain.AppleRefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppleRefreshTokenRepository extends JpaRepository<AppleRefreshToken, Long> {
    Optional<AppleRefreshToken> findByUUID(String uuid);
}