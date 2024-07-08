package com.example.demo.config;

import com.example.demo.member.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig{

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                            .anyRequest().permitAll() //개발 단계에서 모든 요청을 허용
//                        .requestMatchers("/api/v1/member/token").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/swagger-ui/*", "/favicon.ico","/swagger-resources/**","/v3/api-docs/**").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/api/v1/portfolio/**").hasRole("USER") //USER 권한을 가진 사용자들이 접근 가능한 METHOD 및 URL 설정
//                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService);
        return http.getSharedObject(AuthenticationManager.class);
    }
}
