package com.example.demo.chat.domain;

import com.example.demo.base.BaseTimeEntity;
import com.example.demo.enums.member.MemberRole;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.domain.WeddingPlanner;
import jakarta.persistence.*;
import jakarta.servlet.http.PushBuilder;
import jakarta.transaction.Transactional;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.*;

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
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weddingplanner_id")
    private WeddingPlanner weddingPlanner;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_room_id")
    private List<Message> messages = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "chat_room_user", joinColumns = @JoinColumn(name = "chat_room_id"))
    private Set<String> userIds;


    private String lastMessageContent;
    private LocalDateTime lastMessageCreatedAt;

    public void addMessage(Message message) {
        messages.add(message);
        this.lastMessageContent = message.getContent();
        this.lastMessageCreatedAt = message.getCreatedAt();
    }

    // TODO : Redis로 변경 후 삭제
    // append user Id at userIds
    public void addUser(String userId) {
        if (userIds == null) {
            userIds = new HashSet<>();
        }
        userIds.add(userId);
    }

    public void removeUser(String userId) {
        if (userIds == null) {
            return;
        }
        userIds.remove(userId);
    }


}