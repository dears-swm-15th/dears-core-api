package com.example.demo.chat.domain;

import com.example.demo.base.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class ReadFlag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "read_flag_id")
    private Long id;

    private Integer unreadCount;
    private Integer lastReadMessageId;

}
