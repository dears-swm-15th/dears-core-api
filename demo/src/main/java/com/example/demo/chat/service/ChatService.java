package com.example.demo.chat.service;

import com.example.demo.chat.domain.Message;
import com.example.demo.chat.domain.Room;
import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.chat.mapper.MessageMapper;
import com.example.demo.chat.repository.MessageRepository;
import com.example.demo.chat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;

    private final MessageMapper messageMapper;

    public MessageDTO.Response saveMessage(MessageDTO.Request messageRequest) {
        Message message = messageMapper.requestToEntity(messageRequest);
        Message savedMessage = messageRepository.save(message);
        return messageMapper.entityToResponse(savedMessage);
    }

//    public Room createChatRoom(Room chatRoom) {
//        return roomRepository.save(chatRoom);
//    }
//
//    public List<Room> getChatRoomsByMemberId(Long userId) {
//        return roomRepository.findByMemberId(userId);
//    }
//
//    public List<Message> getMessagesByRoomId(Long roomId) {
//        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Chat room not found"));
//        return room.getMessages();
//    }
//

}
