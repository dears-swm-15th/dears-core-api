package com.example.demo.portfolio.domain;

import com.example.demo.base.BaseTimeEntity;
import com.example.demo.enums.OfficeHours;
import com.example.demo.enums.RadarKey;
import com.example.demo.enums.Region;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;


@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Portfolio extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String organization;
    private Region region;
    private String introduction;
    private String contactInfo;
    private String profileImageUrl;
    private Integer consultationFee;
    private String description;

    private Integer avgFee;
    private Integer minFee;
    private Float rating;

    @ElementCollection
    @CollectionTable(name = "portfolio_services", joinColumns = @JoinColumn(name = "portfolio_id"))
    @Column(name = "service_value")
    private List<String> services;

    @ElementCollection
    @CollectionTable(name = "portfolio_wedding_photos", joinColumns = @JoinColumn(name = "portfolio_id"))
    @Column(name = "photo_url")
    private List<String> weddingPhotoUrls;

    @ElementCollection
    @CollectionTable(name = "portfolio_radar", joinColumns = @JoinColumn(name = "portfolio_id"))
    @MapKeyColumn(name = "radar_key")
    @Column(name = "radar_value")
    private Map<RadarKey, Float> radar;

}
