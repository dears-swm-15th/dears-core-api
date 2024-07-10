package com.example.demo.legacychat.service;

import com.example.demo.legacychat.domain.Message;
import com.example.demo.legacychat.domain.Room;
import com.example.demo.legacychat.dto.MessageDTO;
import com.example.demo.legacychat.mapper.MessageMapper;
import com.example.demo.legacychat.repository.MessageRepository;
import com.example.demo.legacychat.repository.RoomRepository;
import com.example.demo.member.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;

    private final CustomUserDetailsService memberService;

    private final MessageMapper messageMapper;

    public void sendAndSaveMessage(MessageDTO.Request messageRequest) {
        Message message = messageMapper.requestToEntity(messageRequest);
        MessageDTO.Response messageResponse = messageMapper.entityToResponse(message);
        messagingTemplate.convertAndSend("/sub/room/" + messageResponse.getRoomId(), messageResponse);

        messageRepository.save(message);
    }

    public Room createChatRoom(Room chatRoom) {
        return roomRepository.save(chatRoom);
    }

//    public List<Room> getChatRoomsOfCurrentMember() {
//        Member member = memberService.getCurrentAuthenticatedMember().orElseThrow();
//
//        return roomRepository.findByMemberId();
//    }
//
//    public List<Message> getMessagesByRoomId(Long roomId) {
//        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Chat room not found"));
//        return room.getMessages();
//    }


}
