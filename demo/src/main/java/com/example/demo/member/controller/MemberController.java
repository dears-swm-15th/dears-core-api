package com.example.demo.member.controller;

import com.example.demo.member.dto.MemberAuthDTO;
import com.example.demo.member.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Tag(name = "member", description = "멤버 API")
public class MemberController {

    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/token")
    @Operation(summary = "토큰이 없을 때 유저 생성")
    public ResponseEntity<MemberAuthDTO.Response> createMember(@RequestBody MemberAuthDTO.Request memberAuthRequest){
        MemberAuthDTO.Response createdMember = customUserDetailsService.join(memberAuthRequest.getRole());
        return ResponseEntity.status(201).body(createdMember);
    }
}
