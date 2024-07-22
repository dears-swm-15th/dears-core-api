package com.example.demo.chat.service;


import com.example.demo.chat.mapper.MessageMapper;
import com.example.demo.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper = MessageMapper.INSTANCE;
}
