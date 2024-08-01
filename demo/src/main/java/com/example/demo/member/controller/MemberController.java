package com.example.demo.member.controller;

import com.example.demo.member.dto.AuthDTO;
import com.example.demo.member.dto.MypageDTO;
import com.example.demo.member.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "member", description = "멤버 API")
@Slf4j
public class MemberController {

    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/auth/shared/create")
    @Operation(summary = "[공통] 토큰이 없을 때 유저 생성", description = "CUSTOMER 또는 WEDDING_PLANNER로 권한을 요청합니다.")
    public ResponseEntity<AuthDTO.Response> createMember(@RequestBody AuthDTO.Request customerAuthRequest) {
        AuthDTO.Response createdMember = customUserDetailsService.join(customerAuthRequest.getRole());
        log.info("Created new member with role: {}", customerAuthRequest.getRole());
        return ResponseEntity.status(201).body(createdMember);
    }

    @GetMapping("/mypage/customer/me")
    @Operation(summary = "[신랑신부] 마이페이지 조회", description = "토큰을 통해 마이페이지 정보를 조회합니다.")
    public ResponseEntity<MypageDTO.CustomerResponse> getCustomerMyPage() {
        MypageDTO.CustomerResponse myPage = customUserDetailsService.getCustomerMyPage();
        log.info("Fetched customer my page");
        return ResponseEntity.status(200).body(myPage);
    }

    @GetMapping("/mypage/weddingplanner/me")
    @Operation(summary = "[웨딩플래너] 마이페이지 조회", description = "토큰을 통해 마이페이지 정보를 조회합니다.")
    public ResponseEntity<MypageDTO.WeddingPlannerResponse> getWeddingPlannerMyPage() {
        MypageDTO.WeddingPlannerResponse myPage = customUserDetailsService.getWeddingPlannerMyPage();
        log.info("Fetched wedding planner my page");
        return ResponseEntity.status(200).body(myPage);
    }

    @PostMapping("/mypage/customer/me")
    @Operation(summary = "[신랑신부] 마이페이지 수정", description = "마이페이지 정보를 수정합니다.")
    public ResponseEntity<MypageDTO.MyPageUpdateResponse> updateCustomerMyPage(@RequestBody MypageDTO.CustomerRequest customerRequest) {
        MypageDTO.MyPageUpdateResponse myPage = customUserDetailsService.updateCustomerMyPage(customerRequest);
        log.info("Updated customer my page");
        return ResponseEntity.status(200).body(myPage);
    }

    @PostMapping("/mypage/weddingplanner/me")
    @Operation(summary = "[웨딩플래너] 마이페이지 수정", description = "마이페이지 정보를 수정합니다.")
    public ResponseEntity<MypageDTO.MyPageUpdateResponse> updateWeddingPlannerMyPage(@RequestBody MypageDTO.WeddingPlannerRequest weddingPlannerRequest) {
        MypageDTO.MyPageUpdateResponse myPage = customUserDetailsService.updateWeddingPlannerMyPage(weddingPlannerRequest);
        log.info("Updated wedding planner my page");
        return ResponseEntity.status(200).body(myPage);
    }

}
