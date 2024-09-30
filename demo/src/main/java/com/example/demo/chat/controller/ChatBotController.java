package com.example.demo.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
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

    private final ChatClient chatClient;

    @PostMapping("/test")
    @Operation(summary = "테스트")
    public ResponseEntity<String> chatWithChatBot(@RequestBody String message) {

        String answer = chatClient.prompt()
                .system("넌 이제부터 한국의 웨딩플래너 전문가야. 추정치를 바탕으로 확실한 답변을 해줘.")
                .user(message)
                .call()
                .content();

        return ResponseEntity.status(200).body(answer);
    }
}
