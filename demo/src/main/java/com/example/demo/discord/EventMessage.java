package com.example.demo.discord;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EventMessage {
    CUSTOMER_SERVICE("고객센터로 문의가 접수되었습니다.");

    private final String message;
}