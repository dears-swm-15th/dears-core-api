package com.example.demo.member.domain;

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
    private String role;

    @Column //중복x, nullx, 멤버 구분자
    private String name;

    public Member (String role, String name) {
        this.role = role;
        this.name = name;
    }
    public Member() {
    }
}
