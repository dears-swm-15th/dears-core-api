package com.example.demo.jwt;

import com.example.demo.error.custom.InvalidJwtAuthenticationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtValidator {

    private final ObjectMapper objectMapper;

    @Value("${jwt.secret}")
    private String secretKey;

    public Map<String, String> parseHeaders(String token) throws JsonProcessingException {
        String header = token.split("\\.")[0];
        return objectMapper.readValue(decodeHeader(header), Map.class);
    }

    public String decodeHeader(String token) {
        return new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
    }

    // 기존 비밀 키 기반의 getTokenClaims 메서드는 그대로 유지
    public Claims getTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)) // 비밀 키 사용
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 공개 키 기반의 getTokenClaims 메서드 추가
    public Claims getTokenClaims(String token, PublicKey publicKey) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey) // 공개 키 사용
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateSignature(String token, PublicKey publicKey) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(publicKey) // 공개 키 사용
                    .build()
                    .parseClaimsJws(token);
            return true; // 시그니처 검증 성공
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.warn("Invalid JWT signature");
            throw new InvalidJwtAuthenticationException("Invalid JWT signature");
        } catch (JwtException e) {
            log.warn("JWT validation error");
            throw new InvalidJwtAuthenticationException("JWT validation error");
        }
    }

    public boolean validatePayload(String token) {
        try {
            Claims claims = getTokenClaims(token);
            Date expiration = claims.getExpiration();
            return expiration != null && expiration.after(new Date()); // 만료 시간 검증
        } catch (Exception e) {
            return false; // 페이로드 검증 실패
        }
    }

    public boolean isValidToken(String token, PublicKey publicKey) {
        return validateSignature(token, publicKey) && validatePayload(token);
    }
}
