package com.example.demo.dummy;

import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.domain.Message;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.enums.chat.MessageType;
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
import com.example.demo.wishlist.domain.WishList;
import com.example.demo.wishlist.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private WishListRepository wishListRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create Portfolios
        List<String> services1 = Arrays.asList("서비스1", "서비스2");
        List<String> services2 = Arrays.asList("서비스A", "서비스B");

        List<String> weddingPhotos1 = Arrays.asList("portfolio/3/7df63e8a-7666-4c92-8c55-fdd26005e97e.jpeg",
                "portfolio/3/6c30499a-f568-416c-8bf9-4e62a20c7cee.jpeg");
        List<String> weddingPhotos2 = Arrays.asList("wedding2_1.jpg", "wedding2_2.jpg");

        WeddingPlanner planner1 = WeddingPlanner.builder()
                .name("Alice")
                .UUID("b1b3825f-304f-4bce-b3b7-91b70fe79cb7")
                .role(WEDDING_PLANNER)
                .build();

        WeddingPlanner planner2 = WeddingPlanner.builder()
                .name("Bob")
                .UUID("c14h814f-33gf-4b4e-z5z7-31b70fe74cb8")
                .role(WEDDING_PLANNER)
                .build();

        Map<RadarKey, Float> radar1 = new HashMap<>();
        radar1.put(RadarKey.COMMUNICATION, 4.5f);
        radar1.put(RadarKey.BUDGET_COMPLIANCE, 3.8f);
        radar1.put(RadarKey.PERSONAL_CUSTOMIZATION, 4.7f);
        radar1.put(RadarKey.PRICE_RATIONALITY, 4.0f);
        radar1.put(RadarKey.SCHEDULE_COMPLIANCE, 4.6f);

        Map<RadarKey, Float> radar2 = new HashMap<>();
        radar2.put(RadarKey.COMMUNICATION, 4.0f);
        radar2.put(RadarKey.BUDGET_COMPLIANCE, 3.9f);
        radar2.put(RadarKey.PERSONAL_CUSTOMIZATION, 4.5f);
        radar2.put(RadarKey.PRICE_RATIONALITY, 4.2f);
        radar2.put(RadarKey.SCHEDULE_COMPLIANCE, 4.8f);

        Portfolio portfolio1 = Portfolio.builder()
                .organization("Organization One")
                .plannerName("Jeff")
                .region(Region.CHUNGDAM)
                .introduction("Introduction to Organization One.")
                .contactInfo("contact@organizationone.com")
                .profileImageUrl("portfolio/3/7310ef17-c1ea-40e1-a786-aa3ddf82b721.jpg")
                .consultingFee(100)
                .description("Description of services offered by Organization One.")
                .estimateSum(300)
                .minEstimate(100)
                .services(services1)
                .weddingPhotoUrls(weddingPhotos1)
                .weddingPlanner(planner1)
                .radarSum(radar1)
                .wishListCount(0)
                .viewCount(0)
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
                .weddingPlanner(planner2)
                .radarSum(radar2)
                .wishListCount(0)
                .viewCount(0)
                .build();



        // Create Reviews
        List<String> reviewTags1 = Arrays.asList("tag1", "tag2");
        List<String> reviewTags2 = Arrays.asList("tagA", "tagB");

        List<String> reviewPhotos1 = Arrays.asList(
                "review/3/d55ac892-e175-4c57-bdd1-250fea3b4364.jpg",
                "review/3/1ae54357-e7e4-4948-beca-3bac9b72b8f7.jpg",
                "review/3/571ba189-6b86-4cd1-89a7-92eb947b8c4e.jpg");
        List<String> reviewPhotos2 = Arrays.asList("review2_1.jpg", "review2_2.jpg");

        Map<RadarKey, Float> reviewRadar1 = new HashMap<>();
        reviewRadar1.put(RadarKey.COMMUNICATION, 4.2f);
        reviewRadar1.put(RadarKey.BUDGET_COMPLIANCE, 3.9f);
        reviewRadar1.put(RadarKey.PERSONAL_CUSTOMIZATION, 4.5f);
        reviewRadar1.put(RadarKey.PRICE_RATIONALITY, 4.0f);
        reviewRadar1.put(RadarKey.SCHEDULE_COMPLIANCE, 4.3f);

        Map<RadarKey, Float> reviewRadar2 = new HashMap<>();
        reviewRadar2.put(RadarKey.COMMUNICATION, 4.0f);
        reviewRadar2.put(RadarKey.BUDGET_COMPLIANCE, 4.1f);
        reviewRadar2.put(RadarKey.PERSONAL_CUSTOMIZATION, 4.4f);
        reviewRadar2.put(RadarKey.PRICE_RATIONALITY, 4.2f);
        reviewRadar2.put(RadarKey.SCHEDULE_COMPLIANCE, 4.5f);

        Review review1 = Review.builder()
                .content("Great experience with Organization One. Highly recommend!")
                .isProvided(false)
                .reviewerId(1L)
                .rating(4.5f)
                .estimate(350)
                .tags(reviewTags1)
                .weddingPhotoUrls(reviewPhotos1)
                .radar(reviewRadar1)
                .portfolio(portfolio2)
                .build();

        Review review2 = Review.builder()
                .content("Organization Two provided excellent service. Very satisfied!")
                .isProvided(true)
                .reviewerId(1L)
                .rating(4.6f)
                .estimate(450)
                .tags(reviewTags2)
                .weddingPhotoUrls(reviewPhotos2)
                .radar(reviewRadar2)
                .portfolio(portfolio1)
                .build();


        Customer customer1 = Customer.builder()
                .name("Clara")
                .UUID("51fc7d6b-7f86-43cf-b5c7-de4c46046d71")
                .role(CUSTOMER)
                .profileImageUrl("mypage/1/69c76dcc-72ee-40df-bd22-4f81bf1e1afe.jpg")
                .reviewList(Arrays.asList(review1))
                .build();

        Customer customer2 = Customer.builder()
                .name("Jeff")
                .UUID("ed21f25b-f51c-4e07-b1f5-4ffb2d9a0531")
                .role(CUSTOMER)
                .build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        weddingPlannerRepository.save(planner1);
        weddingPlannerRepository.save(planner2);

        portfolioRepository.save(portfolio1);
        portfolioRepository.save(portfolio2);

        reviewRepository.save(review1);
        reviewRepository.save(review2);


        WishList wishList1 = WishList.builder()
                .customer(customer1)
                .portfolio(portfolio1)
                .build();

        WishList wishList2 = WishList.builder()
                .customer(customer2)
                .portfolio(portfolio2)
                .build();

        wishListRepository.save(wishList1);
        wishListRepository.save(wishList2);

        Message message1 = Message.builder()
                .contents("웨딩플래너 님 안녕하세요!")
                .messageType(MessageType.SEND)
                .isDeleted(false)
                .oppositeReadFlag(false)
                .senderRole(CUSTOMER)
                .build();

        ChatRoom chatRoom1 = ChatRoom.builder()
                .customer(customer1)
                .weddingPlanner(planner1)
                .messages(Arrays.asList(message1))
                .isDeleted(false)
                .lastMessageContent("웨딩플래너 님 안녕하세요!")
                .build();

        chatRoomRepository.save(chatRoom1);

        System.out.println("Sample data loaded.");
    }
}
