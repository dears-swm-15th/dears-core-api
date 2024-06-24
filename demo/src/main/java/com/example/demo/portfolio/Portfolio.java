package com.example.demo.portfolio;

import com.example.demo.enums.OfficeHours;
import com.example.demo.enums.Region;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String organization;
    private Region region;
    private String introduction;
    private OfficeHours officeHours;
    private String contactInfo;
    private String image;
    private Integer consultationFee;
    private String description;
    private String weddingPhotos;

    public static PortfolioDTO.Response convertToResponse(Portfolio portfolio) {
        PortfolioDTO.Response response = new PortfolioDTO.Response();
        response.setId(portfolio.getId());
        response.setOrganization(portfolio.getOrganization());
        response.setRegion(portfolio.getRegion().name());
        response.setIntroduction(portfolio.getIntroduction());
        response.setOfficeHours(portfolio.getOfficeHours().name());
        response.setContactInfo(portfolio.getContactInfo());
        response.setImage(portfolio.getImage());
        response.setConsultationFee(portfolio.getConsultationFee());
        response.setDescription(portfolio.getDescription());
        response.setWeddingPhotos(portfolio.getWeddingPhotos());
        return response;
    }

    public static Portfolio convertToEntity(PortfolioDTO.Request request) {
        return Portfolio.builder()
                .organization(request.getOrganization())
                .region(Region.valueOf(request.getRegion()))
                .introduction(request.getIntroduction())
                .officeHours(OfficeHours.valueOf(request.getOfficeHours()))
                .contactInfo(request.getContactInfo())
                .image(request.getImage())
                .consultationFee(request.getConsultationFee())
                .description(request.getDescription())
                .weddingPhotos(request.getWeddingPhotos())
                .build();
    }
}
