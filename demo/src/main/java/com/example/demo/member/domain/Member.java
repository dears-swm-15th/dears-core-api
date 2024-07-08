package com.example.demo.member.domain;

import com.example.demo.enums.member.MemberRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column
    private MemberRole role;

    @Column //중복x, nullx, 멤버 구분자
    private String name;

}
