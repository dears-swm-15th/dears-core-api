package com.example.demo.chat.controller;

import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.chat.service.ChatRoomService;
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
    private final SimpMessagingTemplate template;
    private final MessageService messageService;
    private final ChatRoomService chatRoomService;

    @MessageMapping(value = "/customer/enter/connect")
    @Operation(summary = "[신랑신부] 포트폴리오 아이디로 채팅방 입장(생성 및 입장)")
    public void connectByCustomer(MessageDTO.PortfolioRequest messagePortfolioRequest, @DestinationVariable SimpMessageHeaderAccessor accessor) {
        Long portfolioId = messagePortfolioRequest.getPortfolioId();
        chatRoomService.enterChatRoomByPortfolioId(portfolioId);
        log.info("Entered chat room for customer with portfolio ID: {}", portfolioId);

        // TODO : wp sub 대신 send to
    }


    @MessageMapping(value = "/customer/enter")
    @Operation(summary = "[신랑신부] 채팅방 입장")
    public void enterByCustomer(MessageDTO.Request messageRequest, @DestinationVariable SimpMessageHeaderAccessor accessor) {
        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);

        log.trace("ACCESSOR(SEND): {}", accessor.getSessionAttributes());
        String customerUuid = accessor.getSessionAttributes().get("Authorization").toString();

        messageService.enterChatRoom(messageRequest, customerUuid);

        log.info("Entered chat room with ID: {}", messageRequest.getChatRoomId());
    }

    @MessageMapping(value = "/weddinplanner/enter")
    @Operation(summary = "[웨딩플래너] 채팅방 입장")
    public void enterByWeddingPlanner(MessageDTO.Request messageRequest, @DestinationVariable SimpMessageHeaderAccessor accessor) {
        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);

        log.debug("ACCESSOR(SEND): {}", accessor.getSessionAttributes());
        String customerUuid = accessor.getSessionAttributes().get("Authorization").toString();

        messageService.enterChatRoom(messageRequest, customerUuid);

        log.info("Entered chat room with ID: {}", messageRequest.getChatRoomId());
    }

    @MessageMapping(value = "/customer/send")
    @Operation(summary = "[신랑신부] 메세지 전송")
    public void sendByCustomer(MessageDTO.Request messageRequest, @DestinationVariable SimpMessageHeaderAccessor accessor) {
        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);

        messageService.sendMessageByCustomer(messageRequest);

        log.debug("Customer sent message to chat room with ID: {}", messageRequest.getChatRoomId());
    }

    @MessageMapping(value = "/weddinplanner/send")
    @Operation(summary = "[웨딩플래너] 메세지 전송")
    public void sendByWeddingPlanner(MessageDTO.Request messageRequest) {

        messageService.sendMessageByWeddingPlanner(messageRequest);

        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);
        log.info("WeddingPlanner sent message to chat room with ID: {}", messageRequest.getChatRoomId());
    }

//    @MessageMapping(value = "/leave")
//    @Operation(summary = "채팅방 퇴장")
//    public void leave(MessageDTO.Request messageRequest, @DestinationVariable SimpMessageHeaderAccessor accessor) {
//        // TODO : 방 퇴장 시, 상태 LEAVE로 변경 필요
//
//        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);
//        log.info("Left chat room with ID: {}", messageRequest.getChatRoomId());
//    }

    @MessageMapping(value = "/disconnect")
    @Operation(summary = "채팅방 연결 해제")
    public void disconnect(MessageDTO.Request messageRequest) {
        // TODO : 강제 종료되는 경우에만 실행됨. 후순위 개발.


        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);
        log.info("Disconnected from chat room with ID: {}", messageRequest.getChatRoomId());
    }
}
