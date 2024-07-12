package com.example.demo.portfolio.domain;

import com.example.demo.base.BaseTimeEntity;
import com.example.demo.enums.review.RadarKey;
import com.example.demo.enums.portfolio.Region;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Portfolio extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private Long id;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    // Static fields set by planner
    private String plannerName;
    private String organization;
    private Region region;
    private String introduction;
    private String contactInfo;
    private String profileImageUrl;
    private Integer consultingFee;
    private String description;

    @ElementCollection
    @CollectionTable(name = "portfolio_services", joinColumns = @JoinColumn(name = "portfolio_id"))
    @Column(name = "service_value")
    private List<String> services;

    @ElementCollection
    @CollectionTable(name = "portfolio_wedding_photos", joinColumns = @JoinColumn(name = "portfolio_id"))
    @Column(name = "photo_url")
    private List<String> weddingPhotoUrls;

    // Dynamic changed fields
    private Float ratingSum;
    private Integer ratingCount;

    private Float estimateSum;
    private Integer estimateCount;
    private Integer minEstimate;

    @ElementCollection
    @CollectionTable(name = "portfolio_radar", joinColumns = @JoinColumn(name = "portfolio_id"))
    @MapKeyColumn(name = "radar_key")
    @Column(name = "radar_value")
    private Map<RadarKey, Float> radarSum;
    private Integer radarCount;

    private Integer wishListCount;


    public void increaseWishListCount() {
        if (this.wishListCount == null) {
            this.wishListCount = 0;
        }
        this.wishListCount++;
    }

    public void increaseRatingCount() {
        if (this.ratingCount == null) {
            this.ratingCount = 0;
        }
        this.ratingCount++;
    }

    public void increaseEstimateCount() {
        if (this.estimateCount == null) {
            this.estimateCount = 0;
        }
        this.estimateCount++;
    }

    public void increaseRadarCount() {
        if (this.radarCount == null) {
            this.radarCount = 0;
        }
        this.radarCount++;
    }

    public void accumulateRatingSum(Float addition) {
        this.ratingSum += addition;
    }

    public void accumulateEstimate(Float addition) {
        this.estimateSum += addition;
    }

    public void updateMinEstimate(Integer minEstimate) {
        this.minEstimate = Math.min(this.minEstimate, minEstimate);
    }

    public void accumulateRadar(Map<RadarKey, Float> addition) {
        addition.forEach((key, value) ->
                this.radarSum.put(key, this.radarSum.get(key) + value));
    }
}

