package com.example.demo.legacychat.service;

import com.example.demo.legacychat.mapper.MessageMapper;
import com.example.demo.legacychat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

//    public MessageDTO.Response getMessageById(Long id) {
//
//    }
}
