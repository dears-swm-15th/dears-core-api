package com.example.demo.dummy;

import com.example.demo.domain.Portfolio;
import com.example.demo.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Override
    public void run(String... args) throws Exception {
        Portfolio portfolio1 = Portfolio.builder()
                .organization("Organization One")
                .region(Portfolio.Region.CHUNGDAM)
                .introduction("Introduction to Organization One.")
                .officeHours(Portfolio.OfficeHours.MORNING)
                .contactInfo("contact@organizationone.com")
                .image("image1.jpg")
                .consultationFee(10000)
                .description("Description of services offered by Organization One.")
                .weddingPhotos("wedding1.jpg")
                .build();

        Portfolio portfolio2 = Portfolio.builder()
                .organization("Organization Two")
                .region(Portfolio.Region.GANGNAM)
                .introduction("Introduction to Organization Two.")
                .officeHours(Portfolio.OfficeHours.AFTERNOON)
                .contactInfo("contact@organizationtwo.com")
                .image("image2.jpg")
                .consultationFee(20000)
                .description("Description of services offered by Organization Two.")
                .weddingPhotos("wedding2.jpg")
                .build();

        portfolioRepository.save(portfolio1);
        portfolioRepository.save(portfolio2);

        System.out.println("Sample data loaded.");
    }
}
