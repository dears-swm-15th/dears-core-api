package com.teamdears.core.chat.service;


import com.teamdears.core.chat.domain.ChatRoom;
import com.teamdears.core.chat.domain.Message;
import com.teamdears.core.chat.dto.MessageDTO;
import com.teamdears.core.chat.mapper.MessageMapper;
import com.teamdears.core.chat.repository.ChatRoomRepository;
import com.teamdears.core.chat.repository.MessageRepository;
import com.teamdears.core.config.StompPreHandler;
import com.teamdears.core.enums.member.MemberRole;
import com.teamdears.core.member.domain.Customer;
import com.teamdears.core.member.domain.WeddingPlanner;
import com.teamdears.core.redis.service.RedisService;
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
    private final ChatRoomRepository chatRoomRepository;

    private final SimpMessagingTemplate template;
    private final RedisService redisService;


    @Transactional
    public MessageDTO.Response sendMessageByCustomer(MessageDTO.Request messageRequest, String customerUuid) {
        messageRequest.setSenderRole(MemberRole.CUSTOMER);

        ChatRoom chatRoom = chatRoomRepository.findById(messageRequest.getChatRoomId()).orElseThrow();
        WeddingPlanner weddingPlanner = chatRoom.getWeddingPlanner();
        boolean isOppositeConnected = StompPreHandler.isUserConnected(weddingPlanner.getUUID());

        if (!isOppositeConnected) {
            // TODO : FCM
        }
        return sendMessage(messageRequest, customerUuid);
    }

    @Transactional
    public MessageDTO.Response sendMessageByWeddingPlanner(MessageDTO.Request messageRequest, String weddingPlannerUuid) {
        messageRequest.setSenderRole(MemberRole.WEDDING_PLANNER);

        ChatRoom chatRoom = chatRoomRepository.findById(messageRequest.getChatRoomId()).orElseThrow();
        Customer customer = chatRoom.getCustomer();
        boolean isOppositeConnected = StompPreHandler.isUserConnected(customer.getUUID());

        if (!isOppositeConnected) {
            // TODO : FCM
        }
        return sendMessage(messageRequest, weddingPlannerUuid);
    }

    public MessageDTO.Response sendMessage(MessageDTO.Request messageRequest, String Uuid) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(messageRequest.getChatRoomId());
        Message message = messageMapper.requestToEntity(messageRequest);

        template.convertAndSend("/sub/" + messageRequest.getChatRoomId(), messageRequest);

        if (redisService.getSetSize(chatRoom.getId().toString()) == 2) {
            message.setOppositeReadFlag(true);
        } else {
            message.setOppositeReadFlag(false);
        }

        messageRepository.save(message);

        chatRoom.addMessage(message);
        chatRoom.setLastMessageContent(message.getContent());
        chatRoom.setLastMessageCreatedAt(message.getCreatedAt());

        chatRoomRepository.save(chatRoom);

        return messageMapper.entityToResponse(message);
    }

    public void leaveChatRoomForCustomer(MessageDTO.Request messageRequest, String customerUuid) {
        log.info("Leaving chat room for customer with chat room ID: {}", messageRequest.getChatRoomId());
        ChatRoom chatRoom = chatRoomRepository.findById(messageRequest.getChatRoomId())
                .orElseThrow();

        redisService.deleteSetValue(chatRoom.getId().toString(), customerUuid);
        chatRoomRepository.save(chatRoom);
    }

    public void leaveChatRoomForWeddingPlanner(MessageDTO.Request messageRequest, String weddingPlannerUuid) {
        log.info("Leaving chat room for wedding planner with chat room ID: {}", messageRequest.getChatRoomId());
        ChatRoom chatRoom = chatRoomRepository.findById(messageRequest.getChatRoomId())
                .orElseThrow();

        redisService.deleteSetValue(chatRoom.getId().toString(), weddingPlannerUuid);
        chatRoomRepository.save(chatRoom);
    }


}
