package com.example.demo.legacychat.controller;

import com.example.demo.legacychat.domain.Room;
import com.example.demo.legacychat.dto.MessageDTO;
import com.example.demo.legacychat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@Tag(name = "chat", description = "채팅 API")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/{customerId}/{")
    @Operation(summary = "채팅방 생성")
    public Room createChatRoom(@RequestBody Room chatRoom) {
        return chatService.createChatRoom(chatRoom);
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "유저(고객/웨딩플래너) ID로 채팅방 목록 조회")
//
//    @GetMapping("/rooms/{userId}")
//    public List<Room> getChatRoomsByUserId(@PathVariable Long userId) {
//        return chatService.getChatRoomsByUserId(userId);
//    }
//
//    @GetMapping("/rooms/messages/{roomId}")
//    public List<Message> getMessagesByRoomId(@PathVariable Long roomId) {
//        return chatService.getMessagesByRoomId(roomId);
//    }


    // /pub/message 에 메세지가 오면 동작
    @MessageMapping(value = "/send/message")
    public void message(MessageDTO.Request messageRequest){
        chatService.sendAndSaveMessage(messageRequest);
    }


}
