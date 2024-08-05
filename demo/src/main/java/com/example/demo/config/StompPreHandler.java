package com.example.demo.config;

import com.example.demo.member.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StompPreHandler implements ChannelInterceptor {

    private final CustomUserDetailsService customUserDetailsService;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

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

        System.out.println("COMMAND: "+accessor.getCommand());
        System.out.println("ACCESSOR: "+accessor.toString());
        System.out.println("HEADER: "+accessor.getNativeHeader(AUTHORIZATION_HEADER));
        System.out.println("SESSION: "+accessor.getSessionAttributes());

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
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
        } else if (StompCommand.SEND.equals(accessor.getCommand())) {

            message = MessageBuilder.createMessage(message.getPayload(), accessor.toMessageHeaders());
            System.out.println("MESSAGE: "+message.getHeaders());
            return message;

        }
        return message;
    }
}
