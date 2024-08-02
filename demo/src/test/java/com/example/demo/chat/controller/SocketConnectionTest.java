package com.example.demo.chat.controller;

import com.example.demo.chat.domain.Message;
import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.config.OpenSearchConfig;
import com.example.demo.config.S3Config;
import com.example.demo.config.S3Uploader;
import com.example.demo.config.SecurityConfig;
import com.example.demo.enums.chat.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

// Port number is random, @LocalServerPort is used to retrieve the port number.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import({SecurityConfig.class})
@Slf4j
class SocketConnectionTest {

    @MockBean
    private S3Uploader s3Uploader;

    @MockBean
    private OpenSearchConfig openSearchConfig;

    @MockBean
    private OpenSearchClient openSearchClient;


    @LocalServerPort
    private int port;

    private String URL;

//    private BlockingQueue<String> blockingQueue;

    private WebSocketStompClient stompClient;

    @BeforeEach
    public void setup() {
        URL = "ws://localhost:" + port + "/stomp/chat";
        blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    public void testWebSocketConnection() throws Exception {
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", "51fc7d6b-7f86-43cf-b5c7-de4c46046d71");

        StompSession session = stompClient.connect(URL, new WebSocketHttpHeaders(), connectHeaders, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                System.out.println("Connected to the WebSocket server.");
            }
        }).get(1, TimeUnit.SECONDS);

        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();

        StompHeaders subscribeHeaders = new StompHeaders();
        subscribeHeaders.setDestination("/sub/1");
        subscribeHeaders.add("Authorization", "51fc7d6b-7f86-43cf-b5c7-de4c46046d71");

        session.subscribe(subscribeHeaders, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.offer((String) payload);
            }
        });

        StompHeaders sendHeaders = new StompHeaders();
        sendHeaders.setDestination("/pub/send");
        sendHeaders.add("Authorization", "51fc7d6b-7f86-43cf-b5c7-de4c46046d71");

        // The JSON body of the STOMP message
        MessageDTO.Request body = MessageDTO.Request.builder()
                .id(1L)
                .messageType(MessageType.SEND)
                .contents("Hello, World!")
                .chatRoomId(1L)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(body);
        System.out.println("Sending message: " + jsonBody);
        session.send(sendHeaders, jsonBody);

        System.out.println(blockingQueue);

        String message = blockingQueue.poll(5, TimeUnit.SECONDS);
        System.out.println("Received message: " + message);
        assertThat(message).isEqualTo("Hello, World!");
    }
}
