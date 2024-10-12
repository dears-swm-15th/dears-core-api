package com.teamdears.core.chat.domain;

import com.teamdears.core.base.BaseTimeEntity;
import com.teamdears.core.enums.chat.MessageType;
import com.teamdears.core.enums.member.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    private MessageType messageType;
    private String content;

    private boolean oppositeReadFlag;


    @Enumerated(EnumType.STRING)
    private MemberRole senderRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

}
