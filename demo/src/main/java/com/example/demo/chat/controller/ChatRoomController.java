package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ChatRoomDTO;
import com.example.demo.chat.dto.ChatRoomOverviewDTO;
import com.example.demo.chat.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "chatroom", description = "채팅방 API")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/customer/chatroom/{portfolioId}")
    @Operation(summary = "[신랑신부] 포트폴리오 아이디로 채팅방 입장(생성 및 입장)")
    public ResponseEntity<ChatRoomDTO.Response> enterChatRoomByPortfolioIdForCustomer(@PathVariable Long portfolioId) {
        ChatRoomDTO.Response createdChatRoom = chatRoomService.enterChatRoomByPortfolioId(portfolioId);
        return ResponseEntity.status(201).body(createdChatRoom);
    }
    @PostMapping("/weddingplanner/chatroom/{chatRoomId}")
    @Operation(summary = "[웨딩플래너] 채팅방 아이디로 채팅방 입장")
    public ResponseEntity<ChatRoomDTO.Response> getChatRoomByIdForWeddingPlanner(@PathVariable Long chatRoomId) {
        ChatRoomDTO.Response chatRoomByChatRoomId = chatRoomService.getChatRoomByChatRoomId(chatRoomId);
        return ResponseEntity.status(200).body(chatRoomByChatRoomId);
    }

    @GetMapping("/customer/chatroom/get")
    @Operation(summary = "[신랑신부] 현재 모든 채팅방 조회")
    public ResponseEntity<List<ChatRoomOverviewDTO.Response>> getAllChatRoomForCustomer() {
        List<ChatRoomOverviewDTO.Response> currentUsersAllChatRoom = chatRoomService.getCustomersAllChatRoom();
        return ResponseEntity.status(200).body(currentUsersAllChatRoom);
    }

    @GetMapping("/weddingplanner/chatroom/get")
    @Operation(summary = "[웨딩플래너] 현재 모든 채팅방 조회")
    public ResponseEntity<List<ChatRoomOverviewDTO.Response>> getAllChatRoomForWeddingPlanner() {
        List<ChatRoomOverviewDTO.Response> currentUsersAllChatRoom = chatRoomService.getWeddingPlannersAllChatRoom();
        return ResponseEntity.status(200).body(currentUsersAllChatRoom);
    }

    // TODO : 현재 유저만 나가야 함.
    // TODO : 메소드 명, API route 변경
    @PostMapping("/delete/{chatRoomId}")
    @Operation(summary = "특정 채팅방 나가기")
    public ResponseEntity<Void> deleteChatRoom(Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseEntity.noContent().build();
    }
}
