package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ChatRoomDTO;
import com.example.demo.chat.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatroom")
@Tag(name = "chatroom", description = "채팅방 API")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/{portfolioId}")
    @Operation(summary = "포트폴리오 아이디로 채팅방 입장(생성 및 입장)")
    public ResponseEntity<ChatRoomDTO.Response> createChatRoomByPortfolioId(Long portfolioId) {
        ChatRoomDTO.Response createdChatRoom = chatRoomService.enterChatRoomByPortfolioId(portfolioId);
        return ResponseEntity.status(201).body(createdChatRoom);
    }

    @GetMapping("")
    @Operation(summary = "현재 사용자의 모든 채팅방 조회")
    public ResponseEntity<List<ChatRoomDTO.Response>> getCurrentUsersAllChatRoom() {
        List<ChatRoomDTO.Response> currentUsersAllChatRoom = chatRoomService.getCurrentUsersAllChatRoom();
        return ResponseEntity.status(200).body(currentUsersAllChatRoom);
    }

    @PostMapping("/delete/{chatRoomId}")
    @Operation(summary = "특정 채팅방 나가기")
    public ResponseEntity<Void> deleteChatRoom(Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseEntity.noContent().build();
    }
}
