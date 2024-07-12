package com.example.demo.chat.domain;

import com.example.demo.member.domain.WeddingPlanner;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

@Entity
@DiscriminatorValue("WEDDINGPLANNER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Where(clause = "is_deleted = false")
public class WeddingPlannerMessage extends Message {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weddingplanner_id")
    private WeddingPlanner weddingplanner;

}
