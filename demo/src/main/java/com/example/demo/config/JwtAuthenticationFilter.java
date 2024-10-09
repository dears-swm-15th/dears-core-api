package com.example.demo.config;

import com.example.demo.jwt.JwtValidator;
import com.example.demo.member.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;


@Component
@RequiredArgsConstructor
class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtValidator jwtValidator;


    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = resolveToken(request);

        // validate JWT token
        if (accessToken != null && jwtValidator.isValidToken(accessToken)) {
            Authentication authentication = getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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
}