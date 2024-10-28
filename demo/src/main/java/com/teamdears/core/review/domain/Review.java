package com.teamdears.core.review.domain;

import com.teamdears.core.base.BaseTimeEntity;
import com.teamdears.core.enums.review.RadarKey;
import com.teamdears.core.portfolio.domain.Portfolio;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    private Long reviewerId;
    private String reviewerName;
    private String reviewerNickname;
    private String content;
    private Boolean isProvided;

    private Float rating;
    private Integer estimate;

    @ElementCollection
    @CollectionTable(name = "review_tags", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "tag_value")
    private List<String> tags;
    // tag: name or code?
    // colorCode: at view or server?

    @ElementCollection
    @CollectionTable(name = "review_photos", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "photo_url")
    private List<String> weddingPhotoUrls;

    @ElementCollection
    @CollectionTable(name = "review_radar", joinColumns = @JoinColumn(name = "review_id"))
    @MapKeyColumn(name = "radar_key")
    @Column(name = "radar_value")
    private Map<RadarKey, Float> radar;

    @ManyToOne
    private Portfolio portfolio;

}
