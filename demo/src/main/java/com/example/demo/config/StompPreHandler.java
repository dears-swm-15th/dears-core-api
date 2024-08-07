package com.example.demo.config;

import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.enums.chat.MessageType;
import com.example.demo.member.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StompPreHandler implements ChannelInterceptor {

    private final CustomUserDetailsService customUserDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 메시지가 채널로 전송되기 전에 실행
     *
     * @param message 메시지 객체
     * @param channel 메시지 채널
     * @return 수정된 메시지 객체
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        log.info("ACCESSOR: {}", accessor);

        // 메시지의 구독 명령이 CONNECT인 경우에만 실행
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("STOMP CONNECTED");
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

            List<String> headers = headerAccessor.getNativeHeader(AUTHORIZATION_HEADER);

            if (headers != null && !headers.isEmpty()) {

                Map<String, Object> attributes = headerAccessor.getSessionAttributes();
                attributes.put(AUTHORIZATION_HEADER, headers.get(0).toString());
                headerAccessor.setSessionAttributes(attributes);

                return message;
            } else {
                return message;
            }
        }

        if (StompCommand.SEND.equals(accessor.getCommand())) {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
            String authHeader = accessor.getSessionAttributes().get("Authorization").toString();
            log.info("AUTH HEADER: " + authHeader);

            if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
                String jwt = authHeader.substring(BEARER_PREFIX.length());
                String username = jwt;
            }
        }

        return message;
    }
}
