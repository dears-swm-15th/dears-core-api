package com.example.demo.portfolio;

import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.demo.enums.Region;
import com.example.demo.enums.OfficeHours;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "organization", nullable = false, length = 20)
    private String organization;

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    private Region region;

    @Column(name = "introduction", nullable = false, length = 100)
    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(name = "office_hours", nullable = false)
    private OfficeHours officeHours;

    @Column(name = "contact_info", nullable = false, length = 100)
    private String contactInfo;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @Column(name = "consultation_fee")
    private Integer consultationFee;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "wedding_photos", columnDefinition = "TEXT")
    private String weddingPhotos;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "wp_id")
//    private Long wpId;
}
