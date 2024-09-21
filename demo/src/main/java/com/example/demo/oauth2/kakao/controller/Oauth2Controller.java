package com.example.demo.oauth2.kakao.controller;

import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.oauth2.kakao.dto.KakaoUserInfoResponseDto;
import com.example.demo.oauth2.kakao.dto.LoginDTO;
import com.example.demo.oauth2.kakao.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/oauth2")
@Tag(name = "oauth2", description = "OAuth2 로그인 API")
public class Oauth2Controller {

    private final KakaoService kakaoService;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/shared/kakao")
    @Operation(summary = "[공통] 카카오 로그인")
    public ResponseEntity<LoginDTO.Response> kakaoLogin(@RequestBody LoginDTO.Request loginRequest) {
        String kakaoAccessToken = kakaoService.getAccessTokenFromKakaoDeploy(loginRequest.getCode());
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(kakaoAccessToken);
        String role = loginRequest.getRole();

        LoginDTO.Response loginResponse = customUserDetailsService.createKakaoMember(userInfo, role);
        return ResponseEntity.status(200).body(loginResponse);
    }
}
