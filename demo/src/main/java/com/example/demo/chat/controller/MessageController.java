package com.example.demo.chat.controller;

import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.chat.service.ChatRoomService;
import com.example.demo.chat.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/v1/message")
@Tag(name = "message", description = "메세지 API")
public class MessageController {
    private final SimpMessagingTemplate template; //특정 Broker 로 메세지를 전달
    private final MessageService messageService;

    private final ChatRoomService chatRoomService;

    @MessageMapping(value = "/connect")
    public void connect(MessageDTO.Request messageRequest){
        // TODO : 프로그램 실행 시, 모든 채팅방 연결.
        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);
    }

    @MessageMapping(value = "/disconnect")
    public void disconnect(MessageDTO.Request messageRequest){
        // TODO : 강제 종료되는 경우에만 실행됨. 후순위 개발.
        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);
    }

    @MessageMapping(value = "/enter")
    public void enterByChatRoomList(MessageDTO.Request messageRequest){
        // TODO : 방 입장 시, 읽음 처리 필요(readFlag(lastReadMessageId) 갱신 필요)
        // TODO : messages 정보 받아와야 함.
        // TODO(not fixed) : 방 입장 시, 상태 ENTER로 변경 필요
        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);
    }

    @MessageMapping(value = "/send")
    public void send(MessageDTO.Request messageRequest){
        // TODO : readFlag(lastReadMessageId) 갱신 필요

        messageService.saveMessage(messageRequest);

        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);
    }

    @MessageMapping(value = "/leave")
    public void leave(MessageDTO.Request messageRequest) {
        // TODO : 방 퇴장 시, 상태 LEAVE로 변경 필요

        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);
    }

}
