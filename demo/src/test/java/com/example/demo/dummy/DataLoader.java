package com.example.demo.dummy;

import com.example.demo.portfolio.Portfolio;
import com.example.demo.portfolio.PortfolioRepository;
import com.example.demo.enums.Region;
import com.example.demo.enums.OfficeHours;
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
<<<<<<< HEAD
                .region(Portfolio.Region.CHUNGDAM)
                .introduction("Introduction to Organization One.")
                .officeHours(Portfolio.OfficeHours.MORNING)
                .contactInfo("contact@organizationone.com")
                .image("image1.jpg")
                .consultationFee(10000)
=======
                .region(Region.CHUNGDAM)
                .introduction("Introduction to Organization One.")
                .officeHours(OfficeHours.MORNING)
                .contactInfo("contact@organizationone.com")
                .image("image1.jpg")
                .consultationFee(100)
>>>>>>> feat/package-structure
                .description("Description of services offered by Organization One.")
                .weddingPhotos("wedding1.jpg")
                .build();

        Portfolio portfolio2 = Portfolio.builder()
                .organization("Organization Two")
<<<<<<< HEAD
                .region(Portfolio.Region.GANGNAM)
                .introduction("Introduction to Organization Two.")
                .officeHours(Portfolio.OfficeHours.AFTERNOON)
                .contactInfo("contact@organizationtwo.com")
                .image("image2.jpg")
                .consultationFee(20000)
=======
                .region(Region.GANGNAM)
                .introduction("Introduction to Organization Two.")
                .officeHours(OfficeHours.AFTERNOON)
                .contactInfo("contact@organizationtwo.com")
                .image("image2.jpg")
                .consultationFee(200)
>>>>>>> feat/package-structure
                .description("Description of services offered by Organization Two.")
                .weddingPhotos("wedding2.jpg")
                .build();

        portfolioRepository.save(portfolio1);
        portfolioRepository.save(portfolio2);

        System.out.println("Sample data loaded.");
    }
}
