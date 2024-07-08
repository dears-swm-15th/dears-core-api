package com.example.demo.chat.controller;

import com.example.demo.chat.domain.Message;
import com.example.demo.chat.domain.Room;
import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.chat.service.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@Tag(name = "chat", description = "채팅 API")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatService chatService;

//    @MessageMapping("/chat")
//    public void greeting(Message message) {
//        messagingTemplate.convertAndSend("/sub/" + message.getId(), message) ;
//    }
//
//    @PostMapping("/rooms")
//    public Room createChatRoom(@RequestBody Room chatRoom) {
//        return chatService.createChatRoom(chatRoom);
//    }
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

    // stompConfig 에서 설정한 applicationDestinationPrefixes 와 @MessageMapping 경로가 병합됨 (/pub + ...)
    // /pub/enter 에 메세지가 오면 동작
    @MessageMapping(value = "/send/enter")
    public void enter(MessageDTO.Request messageRequest){ // 채팅방 입장
        messageRequest.setContents(messageRequest.getSenderId() + "님이 채팅방에 참여하였습니다.");
        messagingTemplate.convertAndSend("/sub/room/" + messageRequest.getRoomId(), messageRequest);
    }

    // /pub/message 에 메세지가 오면 동작
    @MessageMapping(value = "/send/message")
    public void message(MessageDTO.Request messageRequest){
        MessageDTO.Response savedMessage = chatService.saveMessage(messageRequest);
        messagingTemplate.convertAndSend("/sub/room/" + savedMessage.getRoomId(), savedMessage);
    }


}
