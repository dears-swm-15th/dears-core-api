package com.example.demo.legacychat.domain;

import com.example.demo.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    private Long customerId;

    private Long weddingPlannerId;

//    private Long

    // unreadCount = lastMessageId - lastUnreadMessageId
    private Integer lastUnreadMessageId;

    private String lastMessageContents;

    // last message's createdAt
    private LocalDateTime recentTime;

    @OneToMany(mappedBy = "roomId")
    private List<Message> messages;
}
