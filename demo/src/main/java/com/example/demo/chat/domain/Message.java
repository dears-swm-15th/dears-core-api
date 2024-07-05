package com.example.demo.chat.domain;

import com.example.demo.base.BaseTimeEntity;
import com.example.demo.enums.chat.MessageType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE message SET message.is_deleted = true WHERE message_id = ?")
@Where(clause = "is_deleted = false")
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    private MessageType messageType;
    private Long roomId;
    private Long senderId;
    private String contents;

}
