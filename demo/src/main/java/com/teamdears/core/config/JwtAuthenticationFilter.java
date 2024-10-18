package com.teamdears.core.config;

import com.teamdears.core.error.custom.InvalidJwtAuthenticationException;
import com.teamdears.core.jwt.JwtValidator;
import com.teamdears.core.member.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtValidator jwtValidator;


    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // pass if websocket handshake request
        if (isWebSocketRequest(request)) {
            filterChain.doFilter(request, response); // 필터 통과
            return;
        }

        // pass if reissue token request
        if (shouldSkipFilter(request)) {
            filterChain.doFilter(request, response); // 필터 통과
            return;
        }

        String accessToken = resolveToken(request);

        try {
            log.info("accessToken: {}", accessToken);
            // validate JWT token
            if (accessToken != null && jwtValidator.isValidToken(accessToken)) {
                log.info("accessToken is valid");
                Authentication authentication = getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (InvalidJwtAuthenticationException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            SecurityContextHolder.clearContext();

            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT signature");
            return;
        }

        if (StringUtils.hasText(accessToken)) {
            try {
                // load user by JWT access token
                UserDetails userDetails = customUserDetailsService.loadUserByAccessToken(accessToken);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                logger.error("Could not set user authentication in security context", e);
            }
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

    private Authentication getAuthentication(String token) {
        // 토큰에서 사용자 정보 추출 및 인증 객체 생성
        Claims claims = jwtValidator.getTokenClaims(token);
        String username = claims.getSubject();
        // 권한 또는 사용자 정보 설정
        return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
    }


    // check websocket handshake request
    private boolean isWebSocketRequest(HttpServletRequest request) {
        log.info("request.getRequestURI(): {}", request.getRequestURI());
        String upgradeHeader = request.getHeader("Upgrade");
        return "websocket".equalsIgnoreCase(upgradeHeader);
    }

    // Skip filtering for specific URLs
    private boolean shouldSkipFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/oauth2/shared/reissue");
    }

//    private ApplePublicKeyResponse getAppleAuthPublicKey() throws IOException, InterruptedException {
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://appleid.apple.com/auth/keys"))
//                .GET()
//                .build();
//
//        HttpResponse<String> response = HttpClient.newBuilder()
//                .connectTimeout(Duration.ofSeconds(10))
//                .build()
//                .send(request, HttpResponse.BodyHandlers.ofString());
//
//        if (response.statusCode() == 200) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            return objectMapper.readValue(response.body(), ApplePublicKeyResponse.class);
//        } else {
//            throw new RuntimeException("Failed to retrieve Apple public keys. Status code: " + response.statusCode());
//        }
//    }
}
