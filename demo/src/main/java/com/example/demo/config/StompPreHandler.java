package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Configuration
public class StompPreHandler implements ChannelInterceptor {

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

        // 메시지의 구독 명령이 CONNECT인 경우에만 실행
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

            List<String> headers = headerAccessor.getNativeHeader("Authorization");
            System.out.println("HEADERS:"+headers);

            if (CollectionUtils.isEmpty(headers) ) {
                throw new MessageDeliveryException("UNAUTHORIZED");
            }
        }

        return message;
    }
}
