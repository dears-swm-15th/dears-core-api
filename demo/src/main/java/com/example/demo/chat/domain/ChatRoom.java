package com.example.demo.chat.domain;

import com.example.demo.base.BaseTimeEntity;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.portfolio.domain.Portfolio;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.Where;

@Entity
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
    private WeddingPlanner weddingplanner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

//    @OneToMany(mappedBy = "chatRoom")
//    @OrderBy("createdAt asc")
//    private List<Message> messageLogs = new ArrayList<>();
}