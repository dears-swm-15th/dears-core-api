package com.example.demo.chat.service;


import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.domain.Message;
import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.chat.mapper.ChatRoomMapper;
import com.example.demo.chat.mapper.MessageMapper;
import com.example.demo.chat.repository.MessageRepository;
import com.example.demo.member.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    private final ChatRoomService chatRoomService;

    private final CustomUserDetailsService customUserDetailsService;

    private final ChatRoomMapper chatRoomMapper = ChatRoomMapper.INSTANCE;

    public MessageDTO.Response sendMessageByCustomer(MessageDTO.Request messageRequest, String customerUuid) {

        //TODO customerUuid로 sender 설정

        MessageDTO.Response savedMesageResponse = saveMessage(messageRequest);
        updateReadFlag(savedMesageResponse);

        return savedMesageResponse; // TODO : return 값 확인 필요
    }

    public MessageDTO.Response saveMessage(MessageDTO.Request messageRequest) {

        ChatRoom chatRoom = chatRoomService.getChatRoomById(messageRequest.getChatRoomId());
        Message message = messageMapper.requestToEntity(messageRequest);
        message.setChatRoom(chatRoom);
        Message savedMessage = messageRepository.save(message);

        return messageMapper.entityToResponse(savedMessage);
    }

    public void updateReadFlag(MessageDTO.Response savedMessageResponse) {
        // TODO : readFlag(lastReadMessageId) 갱신 필요
        Long chatRoomId = savedMessageResponse.getChatRoomId();


    }
}
