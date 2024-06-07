package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "Portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "organization", nullable = false)
    private String organization;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "introduction", nullable = false)
    private String introduction;

    @Column(name = "office_hours", nullable = false)
    private String officeHours;

    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    @Column(name = "image")
    private String image;

    @Column(name = "consultation_fee")
    private String consultationFee;

    @Column(name = "description")
    private String description;

    @Column(name = "wedding_photos")
    private String weddingPhotos;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "wp_id")
    private Long wpId;
}