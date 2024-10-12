package com.teamdears.core.member.domain;

import com.teamdears.core.chat.domain.ChatRoom;
import com.teamdears.core.enums.member.MemberRole;
import com.teamdears.core.review.domain.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column
    private String name;

    @Column
    private String nickname;

    @Column
    private String UUID;

    @Column
    private boolean isDeleted;

    @Column
    private String profileImageUrl;

    @OneToMany
    @JoinColumn(name = "customer_id")
    private List<Review> reviewList;

    //매칭 1:N
    @OneToMany
    @JoinColumn(name = "customer_id")
    private List<WeddingPlanner> weddingPlannerList;

    //chatroom 1:N
    @OneToMany
    @JoinColumn(name = "customer_id")
    private List<ChatRoom> chatRooms;

}
