package com.example.demo.oauth2.service;

import com.example.demo.enums.member.MemberRole;
import com.example.demo.jwt.TokenProvider;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.oauth2.dto.ReissueDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.RefreshFailedException;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2Service {

    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    private final RedisTemplate<String, Object> redisTemplateRT;

    @Transactional
    public ReissueDTO.Response reissueJwtToken(ReissueDTO.Request request) throws JsonProcessingException, RefreshFailedException {
        String refreshToken = request.getRefreshToken();

        if (refreshToken == null) {
            throw new NullPointerException("No refresh token found in request");
        }

        tokenProvider.validateRefreshToken(refreshToken);

        long refreshTokenExpiration = tokenProvider.getRefreshTokenExpirationByManual(refreshToken);


        String expiredKey = "expired:" + refreshToken;
        if (refreshTokenExpiration <= 0 || Boolean.TRUE.equals(redisTemplateRT.hasKey(expiredKey))) {

            throw new RefreshFailedException("Refresh token is expired");
        }

        String UUID = tokenProvider.getUniqueId(refreshToken);

        MemberRole memberRole = customUserDetailsService.getCurrentAuthenticatedMemberRole();

        String accessToken = null;
        String newRefreshToken = null;

        if (memberRole == MemberRole.CUSTOMER) {
            Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer();
            accessToken = tokenProvider.createAccessToken(customer.getName(), UUID);
            newRefreshToken = tokenProvider.createRefreshToken(customer.getName(), UUID);

            redisTemplateRT.delete(customer.getUUID());
            redisTemplateRT.opsForValue().set(customer.getUUID(), newRefreshToken, tokenProvider.getRefreshTokenExpiration(refreshToken), TimeUnit.MILLISECONDS);
        } else if (memberRole == MemberRole.WEDDING_PLANNER) {
            WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();
            accessToken = tokenProvider.createAccessToken(weddingPlanner.getName(), UUID);
            newRefreshToken = tokenProvider.createRefreshToken(weddingPlanner.getName(), UUID);

            redisTemplateRT.delete(weddingPlanner.getUUID());
            redisTemplateRT.opsForValue().set(weddingPlanner.getUUID(), newRefreshToken, tokenProvider.getRefreshTokenExpiration(refreshToken), TimeUnit.MILLISECONDS);
        }
        Authentication authentication = customUserDetailsService.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ReissueDTO.Response.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Transactional
    public void logout() {
        MemberRole memberRole = customUserDetailsService.getCurrentAuthenticatedMemberRole();

        String UUID = null;

        if (memberRole == MemberRole.CUSTOMER) {
            Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer();
            UUID = customer.getUUID();
        } else if (memberRole == MemberRole.WEDDING_PLANNER) {
            WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();
            UUID = weddingPlanner.getUUID();
        }

        redisTemplateRT.delete(UUID);
    }

}
