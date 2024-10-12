package com.teamdears.core.config;

import com.teamdears.core.error.custom.InvalidJwtAuthenticationException;
import com.teamdears.core.jwt.JwtValidator;
import com.teamdears.core.member.service.CustomUserDetailsService;
import com.teamdears.core.oauth2.apple.dto.ApplePublicKeyResponse;
import com.teamdears.core.oauth2.apple.service.ApplePublicKeyGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.PublicKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtValidator jwtValidator;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = resolveToken(request);

        try {
            if (accessToken != null) {
                // JWT 헤더를 파싱하고 공개 키를 가져옴
                Map<String, String> headers = jwtValidator.parseHeaders(accessToken);
                PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers, getAppleAuthPublicKey());

                // 공개 키로 서명 검증
                if (jwtValidator.isValidToken(accessToken, publicKey)) {
                    Authentication authentication = getAuthentication(accessToken, publicKey);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (InvalidJwtAuthenticationException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT signature");
            return;
        } catch (Exception e) {
            logger.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Authentication getAuthentication(String token, PublicKey publicKey) {
        // 공개 키를 사용하여 토큰에서 사용자 정보 추출 및 인증 객체 생성
        Claims claims = jwtValidator.getTokenClaims(token, publicKey);
        String username = claims.getSubject();
        // 권한 또는 사용자 정보 설정
        return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
    }

    private ApplePublicKeyResponse getAppleAuthPublicKey() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://appleid.apple.com/auth/keys"))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), ApplePublicKeyResponse.class);
        } else {
            throw new RuntimeException("Failed to retrieve Apple public keys. Status code: " + response.statusCode());
        }
    }
}
