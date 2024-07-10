package com.example.demo.legacychat.service;

import com.example.demo.legacychat.mapper.MessageMapper;
import com.example.demo.legacychat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    private final MessageService messageService;
    private final MessageMapper messageMapper;

//    public RoomDTO.Response getRoomsById(Long id) {
//
//    }

}
