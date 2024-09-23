package com.example.demo.oauth2.controller;

import com.example.demo.member.service.MemberRegistryService;
import com.example.demo.oauth2.google.dto.GoogleLoginDTO;
import com.example.demo.oauth2.google.dto.GoogleUserInfoResponseDTO;
import com.example.demo.oauth2.google.service.GoogleService;
import com.example.demo.oauth2.kakao.dto.KakaoLoginDTO;
import com.example.demo.oauth2.kakao.dto.KakaoUserInfoResponseDTO;
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
    private final GoogleService googleService;

    private final MemberRegistryService memberRegistryService;

    @PostMapping("/shared/kakao")
    @Operation(summary = "[공통] 카카오 로그인")
    public ResponseEntity<KakaoLoginDTO.Response> kakaoLogin(@RequestBody KakaoLoginDTO.Request loginRequest) {
        KakaoUserInfoResponseDTO userInfo = kakaoService.getUserInfo(loginRequest.getKakaoAccessToken());
        String role = loginRequest.getRole();

        KakaoLoginDTO.Response loginResponse = memberRegistryService.createKakaoMember(userInfo, role);
        return ResponseEntity.status(200).body(loginResponse);
    }

    @PostMapping("/shared/google")
    @Operation(summary = "[공통] 구글 로그인")
    public ResponseEntity<GoogleLoginDTO.Response> googleLogin(@RequestBody GoogleLoginDTO.Request loginRequest) {
        GoogleUserInfoResponseDTO userInfo = googleService.getGoogleMemberInfo(loginRequest.getGoogleAccessToken());
        String role = loginRequest.getRole();

        GoogleLoginDTO.Response loginResponse = memberRegistryService.createGoogleMember(userInfo, role);
        return ResponseEntity.status(200).body(loginResponse);
    }


}
