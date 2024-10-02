package com.example.demo.chat.controller;

import com.example.demo.chat.service.ChatBotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/chatbot")
@Tag(name = "chatbot", description = "챗봇 API")
public class ChatBotController {

    private final ChatBotService chatBotService;

    @PostMapping("/test")
    @Operation(summary = "테스트")
    public ResponseEntity<String> chatWithChatBot(@RequestBody String message) {

        String answer = chatBotService.getAnswer(message);

        return ResponseEntity.status(200).body(answer);
    }
}
