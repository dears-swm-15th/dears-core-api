package com.example.demo.member.domain;

import com.example.demo.chat.domain.ChatRoom;
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

    @Builder.Default
    @Column
    private boolean isDeleted = Boolean.FALSE;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column
    private String name;

    @Column
    private String UUID;


    @Column
    private String profileImageUrl;

    @OneToOne(mappedBy = "weddingPlanner", fetch = FetchType.LAZY)
    private Portfolio portfolio; //포트폴리오 먼저 저장하고 웨딩플래너 저장

    //매칭 1:N
    @OneToMany
    @JoinColumn(name = "weddingplanner_id")
    private List<Customer> customerList;

    //chatroom 1:N
    @OneToMany
    @JoinColumn(name = "weddingplanner_id")
    private List<ChatRoom> chatRooms;

    @Column
    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
