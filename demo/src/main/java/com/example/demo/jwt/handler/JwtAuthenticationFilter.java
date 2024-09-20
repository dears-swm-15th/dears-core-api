package com.example.demo.jwt.handler;

import com.example.demo.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenProvider tokenProvider;
}
