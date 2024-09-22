package com.example.demo.admin.controller;

import com.example.demo.admin.dto.AdminDTO;
import com.example.demo.admin.service.AdminService;
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

    @GetMapping("/member/all")
    @Operation(summary = "모든 회원 조회")
    public ResponseEntity<List<AdminDTO.MemberResponse>> getAllMembers() {
        List<AdminDTO.MemberResponse> allMembers = adminService.getAllMembers();
        log.info("Fetched all members");
        return ResponseEntity.status(200).body(allMembers);
    }
}
