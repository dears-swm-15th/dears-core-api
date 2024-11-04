package com.teamdears.core.dummy;

import static com.teamdears.core.enums.member.MemberRole.CUSTOMER;
import static com.teamdears.core.enums.member.MemberRole.WEDDING_PLANNER;

import com.teamdears.core.chat.domain.Message;
import com.teamdears.core.chat.repository.ChatRoomRepository;
import com.teamdears.core.enums.chat.MessageType;
import com.teamdears.core.enums.portfolio.AccompanyType;
import com.teamdears.core.enums.portfolio.Region;
import com.teamdears.core.enums.review.RadarKey;
import com.teamdears.core.member.domain.Customer;
import com.teamdears.core.member.domain.WeddingPlanner;
import com.teamdears.core.member.repository.CustomerRepository;
import com.teamdears.core.member.repository.WeddingPlannerRepository;
import com.teamdears.core.portfolio.domain.Portfolio;
import com.teamdears.core.portfolio.repository.PortfolioRepository;
import com.teamdears.core.review.domain.Review;
import com.teamdears.core.review.repository.ReviewRepository;
import com.teamdears.core.wishlist.repository.WishListRepository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        List<String> services1 = Arrays.asList("퍼스널 컬러 검사 제공", "홀 예약 부케 서비스");
        List<String> services2 = Arrays.asList("서비스A", "서비스B");

        List<String> weddingPhotos1 = Arrays.asList("portfolio/dummy/jeju.jpeg");
        List<String> weddingPhotos2 = Arrays.asList("wedding2_1.jpg", "wedding2_2.jpg");

        // Create Wedding Planners using Builder Pattern
        List<WeddingPlanner> planners = Arrays.asList(
                WeddingPlanner.builder().name("장영환").nickname("jangy").UUID("uuid-1")
                        .profileImageUrl("http://example.com/image1.jpg").role(WEDDING_PLANNER).build(),
                WeddingPlanner.builder().name("문종환").nickname("moon").UUID("uuid-2")
                        .profileImageUrl("http://example.com/image2.jpg").role(WEDDING_PLANNER).build(),
                WeddingPlanner.builder().name("전영서").nickname("junyoung").UUID("uuid-3")
                        .profileImageUrl("http://example.com/image3.jpg").role(WEDDING_PLANNER).build(),
                WeddingPlanner.builder().name("박현진").nickname("parkh").UUID("uuid-4")
                        .profileImageUrl("http://example.com/image4.jpg").role(WEDDING_PLANNER).build(),
                WeddingPlanner.builder().name("최진우").nickname("choij").UUID("uuid-5")
                        .profileImageUrl("http://example.com/image5.jpg").role(WEDDING_PLANNER).build()
        );

        planners.forEach(weddingPlannerRepository::save);

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

        // Create Review Tags
        List<String> reviewTags1 = Arrays.asList("신혼여행", "웨딩홀");
        List<String> reviewTags2 = Arrays.asList("서비스", "추천");
        List<String> reviewTags3 = Arrays.asList("가격만족", "친절함");
        List<String> reviewTags4 = Arrays.asList("맞춤형 상담", "고급스러움");
        List<String> reviewTags5 = Arrays.asList("웨딩드레스", "분위기 좋은");

// Create Review Photos
        List<String> reviewPhotos1 = Arrays.asList();
        List<String> reviewPhotos2 = Arrays.asList();
        List<String> reviewPhotos3 = Arrays.asList();
        List<String> reviewPhotos4 = Arrays.asList();
        List<String> reviewPhotos5 = Arrays.asList();

// Create Review Radar
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

        Map<RadarKey, Float> reviewRadar3 = new HashMap<>();
        reviewRadar3.put(RadarKey.COMMUNICATION, 4.8f);
        reviewRadar3.put(RadarKey.BUDGET_COMPLIANCE, 4.3f);
        reviewRadar3.put(RadarKey.PERSONAL_CUSTOMIZATION, 4.6f);
        reviewRadar3.put(RadarKey.PRICE_RATIONALITY, 4.1f);
        reviewRadar3.put(RadarKey.SCHEDULE_COMPLIANCE, 4.7f);

        Map<RadarKey, Float> reviewRadar4 = new HashMap<>();
        reviewRadar4.put(RadarKey.COMMUNICATION, 4.5f);
        reviewRadar4.put(RadarKey.BUDGET_COMPLIANCE, 4.4f);
        reviewRadar4.put(RadarKey.PERSONAL_CUSTOMIZATION, 4.5f);
        reviewRadar4.put(RadarKey.PRICE_RATIONALITY, 4.3f);
        reviewRadar4.put(RadarKey.SCHEDULE_COMPLIANCE, 4.6f);

        Map<RadarKey, Float> reviewRadar5 = new HashMap<>();
        reviewRadar5.put(RadarKey.COMMUNICATION, 4.3f);
        reviewRadar5.put(RadarKey.BUDGET_COMPLIANCE, 4.2f);
        reviewRadar5.put(RadarKey.PERSONAL_CUSTOMIZATION, 4.4f);
        reviewRadar5.put(RadarKey.PRICE_RATIONALITY, 4.0f);
        reviewRadar5.put(RadarKey.SCHEDULE_COMPLIANCE, 4.5f);

        // Create Portfolios using Builder Pattern
        List<Portfolio> portfolios = Arrays.asList(
                Portfolio.builder().organization("웨딩컨설팅").plannerName("장영환").region(Region.SEOUL)
                        .introduction("친절한 상담").contactInfo("010-1234-5678")
                        .profileImageUrl("portfolio/dummy/636300258690471320-jordanharris.jpeg")
                        .weddingPhotoUrls(weddingPhotos1).services(services1)
                        .description("웨딩컨설팅을 통해 원하는 웨딩을 준비하세요!")
                        .accompanyType(AccompanyType.ACCOMPANY)
                        .radarCount(1).radarSum(radar1)
                        .ratingSum(4.5f).ratingCount(1)
                        .consultingFee(500000).estimateCount(10).estimateSum(50000000).minEstimate(30000000).build(),

                Portfolio.builder().organization("프리미엄 웨딩").plannerName("문종환").region(Region.SEOUL)
                        .introduction("정성을 다하는 서비스").contactInfo("010-2345-6789")
                        .profileImageUrl("portfolio/dummy/636300258690471321-example2.jpeg")
                        .weddingPhotoUrls(weddingPhotos2).services(services2)
                        .description("프리미엄 웨딩과 함께하세요!")
                        .accompanyType(AccompanyType.ACCOMPANY)
                        .radarCount(1).radarSum(radar2)
                        .ratingSum(4.7f).ratingCount(1)
                        .consultingFee(600000).estimateCount(15).estimateSum(9000000).minEstimate(550000).build(),

                Portfolio.builder().organization("럭셔리 웨딩").plannerName("전영서").region(Region.SEOUL)
                        .introduction("신뢰할 수 있는 웨딩 플래너").contactInfo("010-3456-7890")
                        .profileImageUrl("portfolio/dummy/636300258690471322-example3.jpeg")
                        .weddingPhotoUrls(weddingPhotos1).services(services1)
                        .description("럭셔리 웨딩으로 특별한 날을 준비하세요!")
                        .accompanyType(AccompanyType.ACCOMPANY)
                        .radarCount(1).radarSum(radar1)
                        .ratingSum(4.6f).ratingCount(1)
                        .consultingFee(400000).estimateCount(7).estimateSum(2800000).minEstimate(350000).build(),

                Portfolio.builder().organization("럭셔리 웨딩 컨설팅").plannerName("박현진").region(Region.SEOUL)
                        .introduction("프로페셔널한 플래닝").contactInfo("010-4567-8901")
                        .profileImageUrl("portfolio/dummy/636300258690471323-example4.jpeg")
                        .weddingPhotoUrls(weddingPhotos1).services(services1)
                        .description("럭셔리 웨딩 컨설팅의 전문가와 함께하세요!")
                        .accompanyType(AccompanyType.ACCOMPANY)
                        .radarCount(1).radarSum(radar1)
                        .ratingSum(4.8f).ratingCount(1)
                        .consultingFee(750000).estimateCount(20).estimateSum(15000000).minEstimate(500000).build(),

                Portfolio.builder().organization("친절한 웨딩").plannerName("최진우").region(Region.SEOUL)
                        .introduction("꿈같은 웨딩 준비").contactInfo("010-5678-9012")
                        .profileImageUrl("portfolio/dummy/636300258690471324-example5.jpeg")
                        .weddingPhotoUrls(weddingPhotos2).services(services2)
                        .description("친절한 웨딩과 함께하는 행복한 결혼 준비!")
                        .accompanyType(AccompanyType.ACCOMPANY)
                        .radarCount(1).radarSum(radar2)
                        .ratingSum(4.4f).ratingCount(1)
                        .consultingFee(550000).estimateCount(12).estimateSum(6600000).minEstimate(500000).build()
        );

        // Create Reviews
        Review review1 = Review.builder()
                .content("웨딩 플래너님의 세심한 배려 덕분에 신혼여행 준비가 편하게 끝났습니다.")
                .isProvided(false)
                .reviewerId(1L)
                .reviewerNickname("용감한 호랑이 123")
                .rating(4.5f)
                .estimate(350)
                .tags(reviewTags1)
                .weddingPhotoUrls(reviewPhotos1)
                .radar(reviewRadar1)
                .portfolio(portfolios.get(0))
                .build();

        Review review2 = Review.builder()
                .content("예산 내에서 최고의 서비스를 제공받았습니다. 친구들에게도 추천하고 싶어요!")
                .isProvided(true)
                .reviewerId(2L)
                .reviewerNickname("멋진 돌고래 152")
                .rating(4.6f)
                .estimate(400)
                .tags(reviewTags2)
                .weddingPhotoUrls(reviewPhotos2)
                .radar(reviewRadar2)
                .portfolio(portfolios.get(0))
                .build();

        Review review3 = Review.builder()
                .content("결혼 준비로 바쁜데 플래너님이 잘 챙겨주셔서 부담을 덜었어요.")
                .isProvided(true)
                .reviewerId(3L)
                .reviewerNickname("날쎈 다람쥐 102")
                .rating(4.7f)
                .estimate(500)
                .tags(reviewTags3)
                .weddingPhotoUrls(reviewPhotos3)
                .radar(reviewRadar3)
                .portfolio(portfolios.get(0))
                .build();

        Review review4 = Review.builder()
                .content("세심하게 맞춤형 상담을 해주셔서 만족스러운 결혼식을 준비할 수 있었습니다.")
                .isProvided(false)
                .reviewerId(4L)
                .reviewerNickname("훌륭한 코끼리 012")
                .rating(4.8f)
                .estimate(550)
                .tags(reviewTags4)
                .weddingPhotoUrls(reviewPhotos4)
                .radar(reviewRadar4)
                .portfolio(portfolios.get(0))
                .build();

        Review review5 = Review.builder()
                .content("웨딩드레스와 장소까지 완벽하게 추천해 주셔서 감동했습니다.")
                .isProvided(true)
                .reviewerId(5L)
                .reviewerNickname("느린 하마 812")
                .rating(4.4f)
                .estimate(300)
                .tags(reviewTags5)
                .weddingPhotoUrls(reviewPhotos5)
                .radar(reviewRadar5)
                .portfolio(portfolios.get(0))
                .build();

        // Map each portfolio to corresponding planner and save
        for (int i = 0; i < planners.size(); i++) {
            portfolios.get(i).setWeddingPlanner(planners.get(i));
            portfolios.get(i).setReviews(Arrays.asList(review1, review2, review3, review4, review5));
            portfolioRepository.save(portfolios.get(i));
        }

        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviewRepository.save(review4);
        reviewRepository.save(review5);

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

        Message message1 = Message.builder()
                .content("웨딩플래너 님 안녕하세요!")
                .messageType(MessageType.SEND)
                .isDeleted(false)
                .oppositeReadFlag(true)
                .senderRole(CUSTOMER)
                .build();

        Message message2 = Message.builder()
                .content("안녕하세요! 어떻게 도와드릴까요?")
                .messageType(MessageType.SEND)
                .isDeleted(false)
                .oppositeReadFlag(false)
                .senderRole(WEDDING_PLANNER)
                .build();

        System.out.println("Sample data loaded.");
    }
}
