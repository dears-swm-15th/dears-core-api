package com.example.demo.member.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomerContext extends User { //User implements UserDetails

    private final Customer customer;


    public CustomerContext(Customer customer, Collection<? extends GrantedAuthority> authorities) {
        super(customer.getUUID(), "", authorities);
        this.customer = customer;
    }

    public Customer getMember() {
        return customer;
    }
}
