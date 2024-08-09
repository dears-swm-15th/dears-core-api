package com.example.demo.chat.service;


import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.domain.Message;
import com.example.demo.chat.dto.ChatRoomDTO;
import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.chat.mapper.ChatRoomMapper;
import com.example.demo.chat.mapper.MessageMapper;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.chat.repository.MessageRepository;
import com.example.demo.config.StompPreHandler;
import com.example.demo.enums.member.MemberRole;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    private final ChatRoomService chatRoomService;

    private final ChatRoomMapper chatRoomMapper = ChatRoomMapper.INSTANCE;
    private final ChatRoomRepository chatRoomRepository;

    private final SimpMessagingTemplate template;
    private final CustomerRepository customerRepository;


    @Transactional
    public MessageDTO.Response sendMessageByCustomer(MessageDTO.Request messageRequest, String Uuid) {
        messageRequest.setSenderRole(MemberRole.CUSTOMER);

        ChatRoom chatRoom = chatRoomRepository.findById(messageRequest.getChatRoomId()).orElseThrow();
        WeddingPlanner weddingPlanner = chatRoom.getWeddingPlanner();
        boolean isOppositeConnected = StompPreHandler.isUserConnected(weddingPlanner.getUUID());

        if (isOppositeConnected) {
            return sendMessage(messageRequest, Uuid);
        }
        // TODO : FCM
        return null;
    }

    @Transactional
    public MessageDTO.Response sendMessageByWeddingPlanner(MessageDTO.Request messageRequest, String Uuid) {
        messageRequest.setSenderRole(MemberRole.WEDDING_PLANNER);

        ChatRoom chatRoom = chatRoomRepository.findById(messageRequest.getChatRoomId()).orElseThrow();
        Customer customer = chatRoom.getCustomer();
        boolean isOppositeConnected = StompPreHandler.isUserConnected(customer.getUUID());

        if (isOppositeConnected) {
            return sendMessage(messageRequest, Uuid);
        }
        // TODO : FCM
        return null;
    }

    public MessageDTO.Response sendMessage(MessageDTO.Request messageRequest, String Uuid) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(messageRequest.getChatRoomId());
        Message message = messageMapper.requestToEntity(messageRequest);

        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);

        message.setOppositeReadFlag(true);
        messageRepository.save(message);

        // TODO : Redis로 변경
        chatRoom.addUser(Uuid);
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
