package com.example.demo.oauth2.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginAccessTokenResponseDto {
    private String accessToken;

    private String refreshToken;

    private String UUID;
}
