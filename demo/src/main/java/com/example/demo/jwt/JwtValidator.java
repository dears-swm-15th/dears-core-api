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

    public Claims getTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)) // 비밀 키 사용
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateSignature(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)) // 비밀 키 사용
                    .build()
                    .parseClaimsJws(token);
            return true; // 시그니처 검증 성공
        } catch (io.jsonwebtoken.security.SignatureException e) {
            // SignatureException이 발생한 경우 401 응답을 위해 false를 반환
            log.warn("Invalid JWT signature");
            throw new InvalidJwtAuthenticationException("Invalid JWT signature");
        } catch (JwtException e) {
            // 기타 JwtException 처리
            log.warn("JWT validation error");
            throw new InvalidJwtAuthenticationException("JWT validation error");
        }
    }


    public boolean validatePayload(String token) {
        try {
            Claims claims = getTokenClaims(token);
            // 페이로드 유효성 검증 로직 추가 (예: 만료 시간 확인)
            Date expiration = claims.getExpiration();
            return expiration != null && expiration.after(new Date()); // 만료 시간 검증
        } catch (Exception e) {
            return false; // 페이로드 검증 실패
        }
    }

    public boolean isValidToken(String token) {
        return validateSignature(token) && validatePayload(token);
    }
}
