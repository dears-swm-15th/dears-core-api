package com.example.demo.member.controller;

import com.example.demo.member.dto.AuthDTO;
import com.example.demo.member.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Tag(name = "member", description = "멤버 API")
public class MemberController {

    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/token")
    @Operation(summary = "토큰이 없을 때 유저 생성", description = "CUSTOMER 또는 WEDDINGPLANNER로 권한을 요청합니다.")
    public ResponseEntity<AuthDTO.Response> createMember(@RequestBody AuthDTO.Request customerAuthRequest){
        AuthDTO.Response createdMember = customUserDetailsService.join(customerAuthRequest.getRole());
        return ResponseEntity.status(201).body(createdMember);
    }
}
