package com.example.demo.chat.controller;

import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.chat.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final SimpMessagingTemplate template; //특정 Broker 로 메세지를 전달
    private final MessageService messageService;

    @MessageMapping(value = "/connect")
    @Operation(summary = "채팅방 연결")
    public void connect(MessageDTO.Request messageRequest, @DestinationVariable SimpMessageHeaderAccessor accessor) {
        // TODO : 프로그램 실행 시, 모든 채팅방 연결.
        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);
        log.info("Connected to chat room with ID: {}", messageRequest.getChatRoomId());
    }

    @MessageMapping(value = "/disconnect")
    @Operation(summary = "채팅방 연결 해제")
    public void disconnect(MessageDTO.Request messageRequest) {
        // TODO : 강제 종료되는 경우에만 실행됨. 후순위 개발.
        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);
        log.info("Disconnected from chat room with ID: {}", messageRequest.getChatRoomId());
    }

    @MessageMapping(value = "/enter")
    @Operation(summary = "채팅방 입장")
    public void enterByChatRoomList(MessageDTO.Request messageRequest) {
        // TODO : 방 입장 시, 읽음 처리 필요(readFlag(lastReadMessageId) 갱신 필요)
        // TODO : messages 정보 받아와야 함.
        // TODO(not fixed) : 방 입장 시, 상태 ENTER로 변경 필요
        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);
        log.info("Entered chat room with ID: {}", messageRequest.getChatRoomId());
    }

    @MessageMapping(value = "/customer/send")
    @Operation(summary = "[신랑신부] 메세지 전송")
    public void sendByCustomer(MessageDTO.Request messageRequest, @DestinationVariable SimpMessageHeaderAccessor accessor) {
        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);

        System.out.println("ACCESSOR(SEND): "+accessor.getSessionAttributes());
        messageService.sendMessageByCustomer(messageRequest, accessor.getSessionAttributes().get("Authorization").toString());

        log.info("Sent message to chat room with ID: {}", messageRequest.getChatRoomId());
    }

//    @MessageMapping(value = "/weddinplanner/send")
//    @Operation(summary = "[웨딩플래너] 메세지 전송")
//    public void sendByWeddinplanner(MessageDTO.Request messageRequest) {
//        // TODO : readFlag(lastReadMessageId) 갱신 필요
//
//        messageService.saveMessage(messageRequest);
//
//        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);
//        log.info("Sent message to chat room with ID: {}", messageRequest.getChatRoomId());
//    }

    @MessageMapping(value = "/leave")
    @Operation(summary = "채팅방 퇴장")
    public void leave(MessageDTO.Request messageRequest) {
        // TODO : 방 퇴장 시, 상태 LEAVE로 변경 필요

        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);
        log.info("Left chat room with ID: {}", messageRequest.getChatRoomId());
    }
}
