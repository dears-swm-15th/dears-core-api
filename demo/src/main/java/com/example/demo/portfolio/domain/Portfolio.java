package com.example.demo.portfolio.domain;

import com.example.demo.base.BaseTimeEntity;
import com.example.demo.enums.review.RadarKey;
import com.example.demo.enums.portfolio.Region;
import com.example.demo.wishlist.domain.WishList;
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
    private Integer avgEstimate;
    private Integer minEstimate;

    @ElementCollection
    @CollectionTable(name = "portfolio_radar", joinColumns = @JoinColumn(name = "portfolio_id"))
    @MapKeyColumn(name = "radar_key")
    @Column(name = "radar_value")
    private Map<RadarKey, Float> avgRadar;

    @OneToMany(mappedBy = "portfolio")
    private List<WishList> wishList;

}

