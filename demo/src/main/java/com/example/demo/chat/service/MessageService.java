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
import com.example.demo.member.domain.Customer;
import com.example.demo.member.service.CustomUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    private final ChatRoomService chatRoomService;

    private final CustomUserDetailsService customUserDetailsService;

    private final ChatRoomMapper chatRoomMapper = ChatRoomMapper.INSTANCE;
    private final ChatRoomRepository chatRoomRepository;


    @Transactional
    public MessageDTO.Response sendMessageByCustomer(MessageDTO.Request messageRequest) {
        messageRequest.setSenderRole(MemberRole.CUSTOMER);

        return saveMessage(messageRequest);
    }

    @Transactional
    public MessageDTO.Response sendMessageByWeddingPlanner(MessageDTO.Request messageRequest) {
        messageRequest.setSenderRole(MemberRole.WEDDING_PLANNER);

        return saveMessage(messageRequest);
    }

    public MessageDTO.Response saveMessage(MessageDTO.Request messageRequest) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(messageRequest.getChatRoomId());
        Message message = messageMapper.requestToEntity(messageRequest);

        // Set readCount based on participants
        if (chatRoom.getUserIds().size() > 1) {
            message.setReadCount(1); // Assuming the sender has read the message
        } else {
            message.setReadCount(0);
        }

        messageRepository.save(message);

        chatRoom.addMessage(message);

        chatRoomRepository.save(chatRoom);

        return messageMapper.entityToResponse(message);
    }

    public ChatRoomDTO.Response enterChatRoom(MessageDTO.Request messageRequest, String Uuid) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(messageRequest.getChatRoomId());

        chatRoom.addUser(Uuid);

        // set every readCount to 0
        chatRoom.getMessages().forEach(message -> message.setReadCount(0));

        chatRoomRepository.save(chatRoom);

        return chatRoomMapper.entityToResponse(chatRoom);
    }

}
