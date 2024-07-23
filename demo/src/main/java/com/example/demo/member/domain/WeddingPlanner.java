package com.example.demo.member.domain;

import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.domain.Message;
import com.example.demo.enums.member.MemberRole;
import com.example.demo.portfolio.domain.Portfolio;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DiscriminatorValue("weddingplanner")
public class WeddingPlanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weddingplanner_id")
    private Long id;

    @Column
    private MemberRole role;

    @Column
    private String name;

    @Column
    private String UUID;

    @Column
    private boolean isDeleted;

    @Column
    private String profileImageUrl;

    //포트폴리오 1:1
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weddingplanner_id")
    private Portfolio portfolio;

    //매칭 1:N
    @OneToMany
    @JoinColumn(name = "weddingplanner_id")
    private List<Customer> customerList;

    //chatroom 1:N
    @OneToMany
    @JoinColumn(name = "weddingplanner_id")
    private List<ChatRoom> chatRooms;

}
