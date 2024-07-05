package com.example.demo.member.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class MemberContext extends User { //User implements UserDetails

    private final Member member;


    public MemberContext(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getName(), "", authorities);
        this.member = member;
    }

    public Member getMember() {
        return member;
    }
}
