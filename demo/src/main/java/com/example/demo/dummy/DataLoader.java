package com.example.demo.dummy;

import com.example.demo.enums.RadarKey;
import com.example.demo.enums.Region;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> services1 = Arrays.asList("Service1", "Service2");
        List<String> services2 = Arrays.asList("ServiceA", "ServiceB");

        List<String> weddingPhotos1 = Arrays.asList("wedding1_1.jpg", "wedding1_2.jpg");
        List<String> weddingPhotos2 = Arrays.asList("wedding2_1.jpg", "wedding2_2.jpg");

        Map<RadarKey, Float> radar1 = new HashMap<>();
        radar1.put(RadarKey.COMMUNICATION, 4.5f);
        radar1.put(RadarKey.BUDGET_COMPLIANCE, 3.8f);
        radar1.put(RadarKey.PERSONAL_CUSTOMIZATION, 4.7f);
        radar1.put(RadarKey.PRICE_NATIONALITY, 4.0f);
        radar1.put(RadarKey.SCHEDULE_COMPLIANCE, 4.6f);

        Map<RadarKey, Float> radar2 = new HashMap<>();
        radar2.put(RadarKey.COMMUNICATION, 4.0f);
        radar2.put(RadarKey.BUDGET_COMPLIANCE, 3.9f);
        radar2.put(RadarKey.PERSONAL_CUSTOMIZATION, 4.5f);
        radar2.put(RadarKey.PRICE_NATIONALITY, 4.2f);
        radar2.put(RadarKey.SCHEDULE_COMPLIANCE, 4.8f);

        Portfolio portfolio1 = Portfolio.builder()
                .organization("Organization One")
                .plannerName("Jeff")
                .region(Region.CHUNGDAM)
                .introduction("Introduction to Organization One.")
                .contactInfo("contact@organizationone.com")
                .profileImageUrl("image1.jpg")
                .consultationFee(100)
                .description("Description of services offered by Organization One.")
                .avgFee(300)
                .minFee(100)
                .rating(4.5f)
                .ratingCount(15)
                .feeCount(10)
                .radarCount(5)
                .services(services1)
                .weddingPhotoUrls(weddingPhotos1)
                .radar(radar1)
                .build();

        Portfolio portfolio2 = Portfolio.builder()
                .organization("Organization Two")
                .plannerName("Clara")
                .region(Region.GANGNAM)
                .introduction("Introduction to Organization Two.")
                .contactInfo("contact@organizationtwo.com")
                .profileImageUrl("image2.jpg")
                .consultationFee(200)
                .description("Description of services offered by Organization Two.")
                .avgFee(500)
                .minFee(200)
                .rating(4.3f)
                .ratingCount(20)
                .feeCount(15)
                .radarCount(10)
                .services(services2)
                .weddingPhotoUrls(weddingPhotos2)
                .radar(radar2)
                .build();

        portfolioRepository.save(portfolio1);
        portfolioRepository.save(portfolio2);

        System.out.println("Sample data loaded.");
    }
}
