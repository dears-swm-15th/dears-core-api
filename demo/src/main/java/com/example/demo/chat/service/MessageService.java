package com.example.demo.chat.service;


import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.domain.Message;
import com.example.demo.chat.dto.ChatRoomDTO;
import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.chat.mapper.ChatRoomMapper;
import com.example.demo.chat.mapper.MessageMapper;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.chat.repository.MessageRepository;
import com.example.demo.enums.member.MemberRole;
import com.example.demo.member.service.CustomUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    private final ChatRoomService chatRoomService;

    private final ChatRoomMapper chatRoomMapper = ChatRoomMapper.INSTANCE;
    private final ChatRoomRepository chatRoomRepository;


    @Transactional
    public MessageDTO.Response sendMessageByCustomer(MessageDTO.Request messageRequest, String Uuid) {
        messageRequest.setSenderRole(MemberRole.CUSTOMER);

        return saveMessage(messageRequest, Uuid);
    }

    @Transactional
    public MessageDTO.Response sendMessageByWeddingPlanner(MessageDTO.Request messageRequest, String Uuid) {
        messageRequest.setSenderRole(MemberRole.WEDDING_PLANNER);
        return saveMessage(messageRequest, Uuid);
    }

    public MessageDTO.Response saveMessage(MessageDTO.Request messageRequest, String Uuid) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(messageRequest.getChatRoomId());
        Message message = messageMapper.requestToEntity(messageRequest);

        // TODO : Redis로 변경
        Set<String> userIds = chatRoom.getUserIds();
        userIds.add(Uuid);
        chatRoom.setUserIds(userIds);

        message.setOppositeReadFlag(true);

        messageRepository.save(message);

        chatRoom.addMessage(message);

        chatRoomRepository.save(chatRoom);

        return messageMapper.entityToResponse(message);
    }

    public ChatRoomDTO.Response enterChatRoom(MessageDTO.Request messageRequest, String Uuid) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(messageRequest.getChatRoomId());

        chatRoomRepository.save(chatRoom);

        return chatRoomMapper.entityToResponse(chatRoom);
    }

    public int getAllUnreadMessages(String uuid) {
        return chatRoomService.getCustomersUnreadMessages(uuid);
    }

}
