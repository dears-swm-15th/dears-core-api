package com.example.demo.chat.service;


import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.domain.Message;
import com.example.demo.chat.dto.ChatRoomDTO;
import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.chat.mapper.ChatRoomMapper;
import com.example.demo.chat.mapper.MessageMapper;
import com.example.demo.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    private final ChatRoomService chatRoomService;
    private final ChatRoomMapper chatRoomMapper = ChatRoomMapper.INSTANCE;

    public MessageDTO.Response saveMessage(MessageDTO.Request messageRequest) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(messageRequest.getChatRoomId());
        Message message = messageMapper.requestToEntity(messageRequest);
        message.setChatRoom(chatRoom);
        Message savedMessage = messageRepository.save(message);

        return messageMapper.entityToResponse(savedMessage);
    }
}
