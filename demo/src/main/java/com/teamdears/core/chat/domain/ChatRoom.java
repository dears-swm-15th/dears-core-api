package com.teamdears.core.chat.domain;

import com.teamdears.core.base.BaseTimeEntity;
import com.teamdears.core.member.domain.Customer;
import com.teamdears.core.member.domain.WeddingPlanner;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class ChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weddingplanner_id", referencedColumnName = "weddingplanner_id")
    private WeddingPlanner weddingPlanner;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_room_id")
    private List<Message> messages = new ArrayList<>();

    private String lastMessageContent;
    private LocalDateTime lastMessageCreatedAt;

    public void addMessage(Message message) {
        messages.add(message);
        this.lastMessageContent = message.getContent();
        this.lastMessageCreatedAt = message.getCreatedAt();
    }


}