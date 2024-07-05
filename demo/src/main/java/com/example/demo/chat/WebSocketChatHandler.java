package com.example.demo.chat;

import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.enums.chat.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/*
 * WebSocket Handler
 * sever:clinet => 1:n relation
 * print client message to log, greeting when connect
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    // [!] MAX_CHATROOM_SESSION should scale-out when deploy
    private static final Integer MAX_CHATROOM_SESSION = 3;

    private final ObjectMapper mapper;

    // connecting sessions
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // chatRoomId: {session1, session2}
    private final Map<Long,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    // check socket connection
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // TODO : Auto-generated method stub
        log.info("Websocket Connected | Session ID: {}", session.getId());
        sessions.add(session);
    }

    // handle text message forwarding
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload: {}", payload);

        // convert payload to chatMessageDto
        MessageDTO messageDTO = mapper.readValue(payload, MessageDTO.class);
        log.info("session: {}", messageDTO.toString());

        Long chatRoomId = messageDTO.getRoomId();
        // make session about chatRoom when it doesn't exist at memory
        if(!chatRoomSessionMap.containsKey(chatRoomId)){
            chatRoomSessionMap.put(chatRoomId,new HashSet<>());
        }
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

        // check message type(ENTER)
        if (messageDTO.getMessageType().equals(MessageType.ENTER)) {
            chatRoomSession.add(session);
        }
        // [!] test for 3 chatRoom First.
        if (chatRoomSession.size() >= MAX_CHATROOM_SESSION) {
            removeClosedSession(chatRoomSession);
        }
        sendMessageToChatRoom(messageDTO, chatRoomSession);

    }

    // check session disconnected
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // TODO Auto-generated method stub
        log.info("Websocket Disconnected | Session ID: {}", session.getId());
        sessions.remove(session);
    }

    // ====== chat methods ======
    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(sess -> !sessions.contains(sess));
    }

    private void sendMessageToChatRoom(MessageDTO messageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream().forEach(sess -> sendMessage(sess, messageDto));//2
    }


    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}