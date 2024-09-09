package com.example.demo.config;

import com.example.demo.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StompPreHandler implements ChannelInterceptor {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private static final ConcurrentHashMap<String, String> connectedUsers = new ConcurrentHashMap<>();

    private final RedisService redisService;


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
            log.info("[ STOMP CONNECTED ]");
            List<String> headers = accessor.getNativeHeader(AUTHORIZATION_HEADER);

            log.info("CONNECT | HEADERS: {}", headers);

            if (headers != null && !headers.isEmpty()) {

                Map<String, Object> attributes = accessor.getSessionAttributes();
                String uuid = headers.get(0).toString();

                attributes.put(AUTHORIZATION_HEADER, uuid);

                String sessionId = accessor.getSessionId();
                if (uuid != null) {
                    connectedUsers.put(sessionId, uuid);
                }

                log.info("CONNECT | SESSION ID: {}", sessionId);
                log.info("CONNECT | UUID: {}", uuid);

                accessor.setSessionAttributes(attributes);

                return message;
            } else {
                return message;
            }
        }

        if (StompCommand.SEND.equals(accessor.getCommand())) {
            log.info("[ STOMP SEND ]");

            String authHeader = accessor.getSessionAttributes().get("Authorization").toString();

            log.info("SEND | AUTH HEADER: " + authHeader);

            if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
                String jwt = authHeader.substring(BEARER_PREFIX.length());
                String username = jwt;
            }
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            log.info("[ STOMP SUBSCRIBED ]");
            // get simpDestination
            String simpDestination = accessor.getDestination();
            log.info("SUBSCRIBE | DESTINATION: {}", simpDestination);

        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            log.info("[ STOMP DISCONNECTED ]");

            // get simpDestination
            String simpDestination = accessor.getDestination();
            log.info("DISCONNECT | DESTINATION: {}", simpDestination);

            String sessionId = accessor.getSessionId();
            connectedUsers.remove(sessionId);

            // delete UUID from redis
//            redisService.getKeys();
        }

        return message;
    }

    public static boolean isUserConnected(String uuid) {
        return connectedUsers.containsValue(uuid);
    }
}
