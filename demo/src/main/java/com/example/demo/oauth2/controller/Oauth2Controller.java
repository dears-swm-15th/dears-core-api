package com.example.demo.oauth2.controller;

import com.example.demo.member.service.MemberRegistryService;
import com.example.demo.oauth2.apple.dto.AppleLoginDTO;
import com.example.demo.oauth2.apple.dto.AppleUserInfoResponseDTO;
import com.example.demo.oauth2.apple.service.AppleService;
import com.example.demo.oauth2.dto.ReissueDTO;
import com.example.demo.oauth2.google.dto.GoogleLoginDTO;
import com.example.demo.oauth2.google.dto.GoogleUserInfoResponseDTO;
import com.example.demo.oauth2.google.service.GoogleService;
import com.example.demo.oauth2.kakao.dto.KakaoLoginDTO;
import com.example.demo.oauth2.kakao.dto.KakaoUserInfoResponseDTO;
import com.example.demo.oauth2.kakao.service.KakaoService;
import com.example.demo.oauth2.service.Oauth2Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.RefreshFailedException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/oauth2")
@Tag(name = "oauth2", description = "OAuth2 로그인 API")
public class Oauth2Controller {

    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final AppleService appleService;
    private final Oauth2Service oauth2Service;

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

    @PostMapping("/shared/apple")
    @Operation(summary = "[공통] 애플 로그인")
    public ResponseEntity<AppleLoginDTO.Response> appleLogin(@RequestBody AppleLoginDTO.Request loginRequest) throws AuthenticationException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, InterruptedException {
        AppleUserInfoResponseDTO userInfo = appleService.getAppleUserInfo(loginRequest.getAppleIdToken());
        String role = loginRequest.getRole();

        userInfo.setAuthorizationCode(loginRequest.getAuthorizationCode());
        AppleLoginDTO.Response loginResponse = memberRegistryService.createAppleMember(userInfo, role);
        return ResponseEntity.status(200).body(loginResponse);
    }

//    @GetMapping("/shared/apple/logout")
//    @Operation(summary = "[공통] 애플 로그아웃")
//    public ResponseEntity<Void> appleLogout() {
//        appleService.logout();
//        return ResponseEntity.status(200).build();
//    }

    @PostMapping("/shared/reissue")
    @Operation(summary = "[공통] 리프레시 토큰(RT)을 통한 AT,RT 재발급")
    public ResponseEntity<ReissueDTO.Response> reissueJwtToken(@RequestBody ReissueDTO.Request request) throws RefreshFailedException, JsonProcessingException {
        ReissueDTO.Response reissueResponse = oauth2Service.reissueJwtToken(request);

        return ResponseEntity.status(200).body(reissueResponse);
    }

    @GetMapping("/shared/logout")
    @Operation(summary = "[공통] 로그아웃")
    public ResponseEntity<Void> logout() {
        oauth2Service.logout();
        return ResponseEntity.status(200).build();
    }
}


