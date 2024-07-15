package com.example.demo.dummy;

import com.example.demo.enums.review.RadarKey;
import com.example.demo.enums.portfolio.Region;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.repository.CustomerRepository;
import com.example.demo.member.repository.WeddingPlannerRepository;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.repository.PortfolioRepository;
import com.example.demo.review.domain.Review;
import com.example.demo.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.enums.member.MemberRole.CUSTOMER;
import static com.example.demo.enums.member.MemberRole.WEDDING_PLANNER;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WeddingPlannerRepository weddingPlannerRepository;

    @Autowired
    private CustomerMessageRepository customerMessageRepository;

    @Autowired
    private WeddingPlannerMessageRepository weddingPlannerMessageRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create Portfolios
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
                .consultingFee(100)
                .description("Description of services offered by Organization One.")
                .estimateSum(300)
                .minEstimate(100)
                .services(services1)
                .weddingPhotoUrls(weddingPhotos1)
                .radarSum(radar1)
                .wishListCount(0)
                .build();

        Portfolio portfolio2 = Portfolio.builder()
                .organization("Organization Two")
                .plannerName("Clara")
                .region(Region.GANGNAM)
                .introduction("Introduction to Organization Two.")
                .contactInfo("contact@organizationtwo.com")
                .profileImageUrl("image2.jpg")
                .consultingFee(200)
                .description("Description of services offered by Organization Two.")
                .estimateSum(500)
                .minEstimate(200)
                .services(services2)
                .weddingPhotoUrls(weddingPhotos2)
                .radarSum(radar2)
                .wishListCount(0)
                .build();

        portfolioRepository.save(portfolio1);
        portfolioRepository.save(portfolio2);

        // Create Reviews
        List<String> reviewTags1 = Arrays.asList("tag1", "tag2");
        List<String> reviewTags2 = Arrays.asList("tagA", "tagB");

        List<String> reviewPhotos1 = Arrays.asList("review1_1.jpg", "review1_2.jpg");
        List<String> reviewPhotos2 = Arrays.asList("review2_1.jpg", "review2_2.jpg");

        Map<RadarKey, Float> reviewRadar1 = new HashMap<>();
        reviewRadar1.put(RadarKey.COMMUNICATION, 4.2f);
        reviewRadar1.put(RadarKey.BUDGET_COMPLIANCE, 3.9f);
        reviewRadar1.put(RadarKey.PERSONAL_CUSTOMIZATION, 4.5f);
        reviewRadar1.put(RadarKey.PRICE_NATIONALITY, 4.0f);
        reviewRadar1.put(RadarKey.SCHEDULE_COMPLIANCE, 4.3f);

        Map<RadarKey, Float> reviewRadar2 = new HashMap<>();
        reviewRadar2.put(RadarKey.COMMUNICATION, 4.0f);
        reviewRadar2.put(RadarKey.BUDGET_COMPLIANCE, 4.1f);
        reviewRadar2.put(RadarKey.PERSONAL_CUSTOMIZATION, 4.4f);
        reviewRadar2.put(RadarKey.PRICE_NATIONALITY, 4.2f);
        reviewRadar2.put(RadarKey.SCHEDULE_COMPLIANCE, 4.5f);

        Review review1 = Review.builder()
                .reviewerName("Alice")
                .content("Great experience with Organization One. Highly recommend!")
                .isProvided(true)
                .rating(4.5f)
                .estimate(350)
                .tags(reviewTags1)
                .weddingPhotoUrls(reviewPhotos1)
                .radar(reviewRadar1)
                .build();

        Review review2 = Review.builder()
                .reviewerName("Bob")
                .content("Organization Two provided excellent service. Very satisfied!")
                .isProvided(false)
                .rating(4.6f)
                .estimate(450)
                .tags(reviewTags2)
                .weddingPhotoUrls(reviewPhotos2)
                .radar(reviewRadar2)
                .build();

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        Customer customer1 = Customer.builder()
                .name("Clara")
                .UUID("51fc7d6b-7f86-43cf-b5c7-de4c46046d71")
                .role(CUSTOMER)
                .build();

        Customer customer2 = Customer.builder()
                .name("Jeff")
                .UUID("ed21f25b-f51c-4e07-b1f5-4ffb2d9a0531")
                .role(CUSTOMER)
                .build();

        WeddingPlanner planner1 = WeddingPlanner.builder()
                .name("Alice")
                .UUID("f47ac10b-58cc-4372-a567-0e02b2c3d479")
                .role(WEDDING_PLANNER)
                .build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        weddingPlannerRepository.save(planner1);

//        Customer customer1 = customerRepository.findByName("Clara");
//        Customer customer2 = customerRepository.findByName("Jeff");
//        WeddingPlanner planner1 = weddingPlannerRepository.findByName("Alice");

        CustomerMessage customerMessage1 = CustomerMessage.builder()
                .customer(customer1)
                .contents("Hello, I would like to inquire about your wedding planning services.")
                .unreadCount(1)
                .build();

        CustomerMessage customerMessage2 = CustomerMessage.builder()
                .customer(customer2)
                .contents("Can you provide more details on the packages offered?")
                .unreadCount(2)
                .build();

        WeddingPlannerMessage weddingPlannerMessage1 = WeddingPlannerMessage.builder()
                .weddingplanner(planner1)
                .contents("Sure, we offer a range of packages tailored to your needs.")
                .unreadCount(0)
                .build();

        weddingPlannerMessageRepository.save(weddingPlannerMessage1);
        customerMessageRepository.save(customerMessage1);
        customerMessageRepository.save(customerMessage2);


        System.out.println("Sample data loaded.");
    }
}
