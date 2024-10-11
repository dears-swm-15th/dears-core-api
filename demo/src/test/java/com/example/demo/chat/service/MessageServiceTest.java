package com.example.demo.chat.service;

import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.config.S3Uploader;
import com.example.demo.enums.chat.MessageType;
import com.example.demo.enums.member.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class MessageServiceTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MessageService messageService;

    @MockBean
    private S3Uploader s3Uploader;

    @Test
    @DisplayName("메시지 전송 테스트")
    public void testMessageDelivery() throws Exception {
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandlerAdapter sessionHandler = new StompSessionHandlerAdapter() {
        };
        StompSession session = stompClient.connect("ws://127.0.0.1:8080/stomp/chat/900/iz5furmk/websocket", sessionHandler).get(1, TimeUnit.SECONDS);

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<MessageDTO.Response> receivedMessage = new AtomicReference<>();

        session.subscribe("/sub/1", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return MessageDTO.Response.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                receivedMessage.set((MessageDTO.Response) payload);
                latch.countDown();
            }
        });

        session.send("/pub/1", new MessageDTO.Request(MemberRole.CUSTOMER, MessageType.SEND, "test", 1L));

        latch.await(5, TimeUnit.SECONDS);
        assertNotNull(receivedMessage.get());
    }

    private List<Transport> createTransportClient() {
        return Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
    }


}