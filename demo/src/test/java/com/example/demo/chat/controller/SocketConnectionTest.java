package com.example.demo.chat.controller;

import com.example.demo.config.OpenSearchConfig;
import com.example.demo.config.S3Config;
import com.example.demo.config.S3Uploader;
import com.example.demo.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

// Port number is random, @LocalServerPort is used to retrieve the port number.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import({SecurityConfig.class})
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

    private BlockingQueue<String> blockingQueue;

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
        StompSession session = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);

//        session.subscribe("/sub/1", new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return String.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                blockingQueue.offer((String) payload);
//            }
//        });
//
//        session.send("/pub/1", "World");
//
//        String message = blockingQueue.poll(5, TimeUnit.SECONDS);
//        assertThat(message).isEqualTo("Hello, World!");
    }
}
