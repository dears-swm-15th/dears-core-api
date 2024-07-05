package com.example.demo.chat.dto;

import com.example.demo.enums.chat.MessageType;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {

    private MessageType messageType;
    private Long chatRoomId;
    private Long senderId;
    private String message;

}