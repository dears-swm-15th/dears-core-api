package com.example.demo.member.domain;

import com.example.demo.enums.member.MemberRole;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column
    private MemberRole role;

    @Column //중복x, nullx, 멤버 구분자
    private String name;

    public Member (MemberRole role, String name) {
        this.role = role;
        this.name = name;
    }
    public Member() {
    }
}
