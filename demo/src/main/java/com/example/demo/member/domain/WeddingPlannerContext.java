package com.example.demo.member.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class WeddingPlannerContext extends User { //User implements UserDetails

    private final WeddingPlanner weddingPlanner;


    public WeddingPlannerContext(WeddingPlanner weddingPlanner, Collection<? extends GrantedAuthority> authorities) {
        super(weddingPlanner.getUUID(), "", authorities);
        this.weddingPlanner = weddingPlanner;
    }

    public WeddingPlanner getMember() {
        return weddingPlanner;
    }
}
