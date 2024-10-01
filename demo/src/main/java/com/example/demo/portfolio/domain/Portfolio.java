package com.example.demo.portfolio.domain;

import com.example.demo.base.BaseTimeEntity;
import com.example.demo.enums.portfolio.AccompanyType;
import com.example.demo.enums.portfolio.Region;
import com.example.demo.enums.review.RadarKey;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.review.domain.Review;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
//@Where(clause = "is_deleted = false")
public class Portfolio extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private Long id;

//    @Builder.Default
//    private boolean isDeleted = Boolean.FALSE;

    // Static fields set by planner
    private String plannerName;
    private String organization;
    private Region region;
    private String introduction;
    private String contactInfo;
    private String profileImageUrl;
    private Integer consultingFee;
    private String description;
    private AccompanyType accompanyType;

    @OneToOne
    @JoinColumn(name = "weddingplanner_id")
    private WeddingPlanner weddingPlanner;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "portfolio_services", joinColumns = @JoinColumn(name = "portfolio_id"))
    @Column(name = "service_value")
    private List<String> services = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "portfolio_wedding_photos", joinColumns = @JoinColumn(name = "portfolio_id"))
    @Column(name = "photo_url")
    private List<String> weddingPhotoUrls = new ArrayList<>();

    // Dynamic changed fields
    private Float ratingSum;
    private Integer ratingCount;

    private Integer estimateSum;
    private Integer estimateCount;
    private Integer minEstimate;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "portfolio_radar", joinColumns = @JoinColumn(name = "portfolio_id"))
    @MapKeyColumn(name = "radar_key")
    @Column(name = "radar_value")
    private Map<RadarKey, Float> radarSum;
    private Integer radarCount;

    private Integer wishListCount;

    private Integer viewCount;

    @OneToMany(mappedBy = "portfolio", fetch = FetchType.LAZY)
    private List<Review> reviews;


    public void increaseWishListCount() {
        if (this.wishListCount == null) {
            this.wishListCount = 0;
        }
        this.wishListCount++;
    }

    public void decreaseWishListCount() {
        if (this.wishListCount == null) {
            this.wishListCount = 0;
        } else if (this.wishListCount > 0) {
            this.wishListCount--;
        } else {
            this.wishListCount = 0;
        }
    }

    public void increaseRatingCount(Float rating) {
        if (this.ratingCount == null) {
            this.ratingCount = 0;
        }
        if (rating != null) {
            this.ratingCount++;
        }
    }

    public void increaseEstimateCount(Integer estimate) {
        if (this.estimateCount == null) {
            this.estimateCount = 0;
        }
        if (estimate != null) {
            this.estimateCount++;
        }
    }

    public void increaseRadarCount(Map<RadarKey, Float> radar) {
        if (this.radarCount == null) {
            this.radarCount = 0;
        }
        if (radar != null) {
            this.radarCount++;
        }
    }

    public void accumulateRatingSum(Float addition) {
        if (this.ratingSum == null) {
            this.ratingSum = 0f;
        }
        if (addition == null) {
            addition = 0f;
        }
        this.ratingSum += addition;
    }

    public void accumulateEstimate(Integer addition) {
        if (this.estimateSum == null) {
            this.estimateSum = 0;
        }
        if (addition == null) {
            addition = 0;
        }
        this.estimateSum += addition;
    }

    public void updateMinEstimate(Integer estimate) {
        if (this.minEstimate == null) {
            this.minEstimate = estimate;
        }
        this.minEstimate = Math.min(this.minEstimate, estimate);
    }

    public void accumulateRadarSum(Map<RadarKey, Float> addition) {
        if (this.radarSum == null) {
            this.radarSum = RadarKey.initializeRadarMap();
        }
        if (addition == null) {
            addition = RadarKey.initializeRadarMap();
        }
        addition.forEach((key, value) ->
                this.radarSum.put(key, this.radarSum.get(key) + value));
    }

    public void reduceRatingSum(Float subtraction) {
        if (this.ratingSum != null) {
            this.ratingSum -= subtraction;
        }
    }

    public void reduceEstimateSum(Integer subtraction) {
        if (this.estimateSum != null) {
            this.estimateSum -= subtraction;
        }
    }

    public void reduceRadarSum(Map<RadarKey, Float> subtraction) {
        if (this.radarSum != null) {
            subtraction.forEach((key, value) ->
                    this.radarSum.put(key, this.radarSum.get(key) - value));
        }
    }
}

