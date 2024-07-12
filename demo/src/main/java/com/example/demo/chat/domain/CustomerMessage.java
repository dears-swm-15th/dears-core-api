package com.example.demo.chat.domain;

import com.example.demo.member.domain.Customer;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

@DiscriminatorValue("CUSTOMER")
@Getter
@Setter
@SuperBuilder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class CustomerMessage extends Message {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
