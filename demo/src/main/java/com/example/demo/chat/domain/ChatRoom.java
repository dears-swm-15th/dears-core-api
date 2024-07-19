package com.example.demo.chat.domain;

import com.example.demo.base.BaseTimeEntity;
import com.example.demo.enums.member.MemberRole;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.portfolio.domain.Portfolio;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @JoinColumn(name = "wedding_planner_id")
    private WeddingPlanner weddingPlanner;

    @OneToMany(mappedBy = "chatRoom")
    @OrderBy("createdAt asc")
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    @OrderBy("createdAt asc")
    private Map<MemberRole, ReadFlag> readFlags;

    public void addReadFlag(MemberRole memberRole, ReadFlag readFlag) {
        this.readFlags.put(memberRole, readFlag);
    }
}