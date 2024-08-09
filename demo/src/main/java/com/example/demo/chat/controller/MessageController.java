package com.example.demo.chat.controller;

import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.chat.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final MessageService messageService;

    @MessageMapping(value = "/customer/send")
    @Operation(summary = "[신랑신부] 메세지 전송")
    public void sendByCustomer(MessageDTO.Request messageRequest, @DestinationVariable SimpMessageHeaderAccessor accessor) {
        String customerUuid = accessor.getSessionAttributes().get("Authorization").toString();

        messageService.sendMessageByCustomer(messageRequest, customerUuid);
        log.debug("Customer sent message to chat room with ID: {}", messageRequest.getChatRoomId());
    }

    @MessageMapping(value = "/weddingplanner/send")
    @Operation(summary = "[웨딩플래너] 메세지 전송")
    public void sendByWeddingPlanner(MessageDTO.Request messageRequest, @DestinationVariable SimpMessageHeaderAccessor accessor) {
        String weddingPlannerUuid = accessor.getSessionAttributes().get("Authorization").toString();

        messageService.sendMessageByWeddingPlanner(messageRequest, weddingPlannerUuid);
        log.info("WeddingPlanner sent message to chat room with ID: {}", messageRequest.getChatRoomId());
    }

}
