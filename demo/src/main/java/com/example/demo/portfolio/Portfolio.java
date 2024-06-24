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

    public static PortfolioDTO.Response convertToResponse(Portfolio portfolio) {
        PortfolioDTO.Response response = new PortfolioDTO.Response();
        response.setId(portfolio.getId());
        response.setOrganization(portfolio.getOrganization());
        response.setRegion(portfolio.getRegion().toString());
        response.setIntroduction(portfolio.getIntroduction());
        response.setOfficeHours(portfolio.getOfficeHours().toString());
        response.setContactInfo(portfolio.getContactInfo());
        response.setImage(portfolio.getImage());
        response.setConsultationFee(portfolio.getConsultationFee());
        response.setDescription(portfolio.getDescription());
        response.setWeddingPhotos(portfolio.getWeddingPhotos());
        return response;
    }

    public static Portfolio convertToEntity(PortfolioDTO.Request portfolioRequest) {
        return Portfolio.builder()
                .organization(portfolioRequest.getOrganization())
                .region(Region.valueOf(portfolioRequest.getRegion()))
                .introduction(portfolioRequest.getIntroduction())
                .officeHours(OfficeHours.valueOf(portfolioRequest.getOfficeHours()))
                .contactInfo(portfolioRequest.getContactInfo())
                .image(portfolioRequest.getImage())
                .consultationFee(portfolioRequest.getConsultationFee())
                .description(portfolioRequest.getDescription())
                .weddingPhotos(portfolioRequest.getWeddingPhotos())
                .build();
    }
}
