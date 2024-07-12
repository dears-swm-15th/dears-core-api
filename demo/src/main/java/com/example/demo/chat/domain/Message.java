package com.example.demo.chat.domain;

import com.example.demo.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    private String contents;
    private Integer unreadCount;

}
