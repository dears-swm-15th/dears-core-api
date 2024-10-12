package com.teamdears.core.admin.controller;

import com.teamdears.core.admin.dto.AdminDTO;
import com.teamdears.core.admin.service.AdminService;
import com.teamdears.core.member.service.MemberRegistryService;
import com.teamdears.core.oauth2.kakao.dto.KakaoLoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/admin")
@Tag(name = "admin", description = "관리자 API")
public class AdminController {

    private final AdminService adminService;
    private final MemberRegistryService memberRegistryService;

    @GetMapping("/member/all")
    @Operation(summary = "[Admin] 모든 회원 조회")
    public ResponseEntity<List<AdminDTO.MemberResponse>> getAllMembers() {
        List<AdminDTO.MemberResponse> allMembers = adminService.getAllMembers();
        log.info("Fetched all members");
        return ResponseEntity.status(200).body(allMembers);
    }

    @GetMapping("/user/customer")
    @Operation(summary = "[Admin] Customer 유저 생성")
    public ResponseEntity<KakaoLoginDTO.Response> createAdminCustomer() {
        String username = "admin-customer";
        KakaoLoginDTO.Response reponse = memberRegistryService.createAdminCustomer(username);
        return ResponseEntity.status(200).body(reponse);
    }

    @GetMapping("/user/weddingplanner")
    @Operation(summary = "[Admin] Wedding Planner 유저 생성")
    public ResponseEntity<KakaoLoginDTO.Response> createAdminWeddingPlanner() {
        String username = "admin-weddingplanner";
        KakaoLoginDTO.Response reponse = memberRegistryService.createAdminWeddingPlanner(username);
        return ResponseEntity.status(200).body(reponse);
    }
}
