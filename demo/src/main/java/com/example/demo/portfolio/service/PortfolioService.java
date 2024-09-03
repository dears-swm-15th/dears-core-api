package com.example.demo.portfolio.service;

import com.example.demo.config.S3Uploader;
import com.example.demo.enums.review.RadarKey;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.mapper.WeddingPlannerMapper;
import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioDTO;
import com.example.demo.portfolio.dto.PortfolioOverviewDTO;
import com.example.demo.portfolio.mapper.PortfolioMapper;
import com.example.demo.portfolio.repository.PortfolioRepository;
import com.example.demo.review.domain.Review;
import com.example.demo.review.dto.ReviewDTO;
import com.example.demo.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper = PortfolioMapper.INSTANCE;
    private final WeddingPlannerMapper weddingPlannerMapper = WeddingPlannerMapper.INSTANCE;
    private final ReviewRepository reviewRepository;
    private final S3Uploader s3Uploader;
    private final CustomUserDetailsService customUserDetailsService;

    public List<PortfolioDTO.Response> getAllPortfolios() {
        log.info("Starting getAllPortfolios method");
        return portfolioRepository.findAll().stream()
                .map(portfolioMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public PortfolioDTO.Response getPortfolioById(Long portfolioId) {
        log.info("Starting getPortfolioById method for portfolio ID: {}", portfolioId);
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        PortfolioDTO.Response portfolioResponse = portfolioMapper.entityToResponse(portfolio);

        portfolioResponse.setWeddingPlannerPortfolioResponse(
                weddingPlannerMapper.entityToWeddingPlannerPortfolioDTOResponse(portfolio.getWeddingPlanner())
        );
        portfolioResponse.setAvgRating(calculateAvgRating(portfolioResponse));
        portfolioResponse.setAvgEstimate(calculateAvgEstimate(portfolioResponse));
        portfolioResponse.setAvgRadar(calculateAvgRadar(portfolioResponse));

        log.info("Fetched portfolio with ID: {}", portfolioId);
        return portfolioResponse;
    }

    public List<ReviewDTO.Response> getReviewsByPortfolioId(Long portfolioId) {
        log.info("Starting getReviewsByPortfolioId method for portfolio ID: {}", portfolioId);
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow();
        return reviewRepository.findByPortfolio(portfolio).stream()
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PortfolioDTO.Response createPortfolio(PortfolioDTO.Request portfolioRequest) {

        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();

        if (weddingPlanner.getPortfolio() != null) {
            throw new RuntimeException("Portfolio already exists");
        }
        //일단 id를 가져오기 위한 save
        Portfolio portfolio = portfolioRepository.save(portfolioMapper.requestToEntity(portfolioRequest));

        //weddingplanner 할당
        portfolio.setWeddingPlanner(weddingPlanner);

        //portfolio/{id}/uuid 형식으로 이미지명 생성
        portfolio.setProfileImageUrl(s3Uploader.makeUniqueFileName("portfolio", portfolio.getId(), portfolioRequest.getProfileImageUrl()));
        portfolio.setWeddingPhotoUrls(
                portfolioRequest.getWeddingPhotoUrls().stream()
                        .map(url -> s3Uploader.makeUniqueFileName("portfolio", portfolio.getId(), url))
                        .collect(Collectors.toList())
        );

        //presignedUrl 각각 가져오기
        String profileImagePresignedUrl = s3Uploader.getPresignedUrl(portfolio.getProfileImageUrl());
        List<String> weddingPhotoPresignedUrlList = s3Uploader.getPresignedUrls(portfolio.getWeddingPhotoUrls());

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        PortfolioDTO.Response response = portfolioMapper.entityToResponse(savedPortfolio);

        response.setPresignedProfileImageUrl(profileImagePresignedUrl);
        response.setPresignedWeddingPhotoUrls(weddingPhotoPresignedUrlList);

        return response;
    }

    @Transactional
    public PortfolioDTO.Response updatePortfolio(Long portfolioId, PortfolioDTO.Request portfolioRequest) {
        log.info("Starting updatePortfolio method for portfolio ID: {} with data: {}", portfolioId, portfolioRequest);
        Portfolio existingPortfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));


        String profileImagePresigendUrl = "";
        if (portfolioRequest.getProfileImageUrl() != null && !portfolioRequest.getProfileImageUrl().equals(existingPortfolio.getProfileImageUrl())) {
            //Delete existing image from s3 and upload new image
            s3Uploader.deleteFile(existingPortfolio.getProfileImageUrl());
            existingPortfolio.setProfileImageUrl(s3Uploader.makeUniqueFileName("portfolio", portfolioId, portfolioRequest.getProfileImageUrl()));
            profileImagePresigendUrl = s3Uploader.getPresignedUrl(existingPortfolio.getProfileImageUrl());
        }

        List<String> weddingPhotosPresignedUrlList = new ArrayList<>();
        if (portfolioRequest.getWeddingPhotoUrls() != null) {

            List<String> existingWeddingPhotoUrls = existingPortfolio.getWeddingPhotoUrls();
            List<String> newWeddingPhotoUrls = portfolioRequest.getWeddingPhotoUrls();

            //삭제한 이미지 s3에서 삭제
            Iterator<String> iterator = existingWeddingPhotoUrls.iterator();
            while (iterator.hasNext()) {
                String existingUrl = iterator.next();
                if (!newWeddingPhotoUrls.contains(existingUrl)) {
                    s3Uploader.deleteFile(existingUrl);
                    iterator.remove();
                }
            }

            //새로 추가해야 하는 이미지 s3에 업로드
            newWeddingPhotoUrls.forEach(newUrl -> {
                if (!existingWeddingPhotoUrls.contains(newUrl)) {
                    String newUniqueFilename = s3Uploader.makeUniqueFileName("portfolio", portfolioId, newUrl);
                    existingWeddingPhotoUrls.add(newUniqueFilename);
                    weddingPhotosPresignedUrlList.add(s3Uploader.getPresignedUrl(newUniqueFilename));
                }
            });

            existingPortfolio.setWeddingPhotoUrls(existingWeddingPhotoUrls);
        }

        Portfolio savedPortfolio = portfolioRepository.save(existingPortfolio);
        PortfolioDTO.Response response = portfolioMapper.entityToResponse(savedPortfolio);

        if (profileImagePresigendUrl != null) {
            response.setPresignedProfileImageUrl(profileImagePresigendUrl);
        }
        if (!weddingPhotosPresignedUrlList.isEmpty()) {
            response.setPresignedWeddingPhotoUrls(weddingPhotosPresignedUrlList);
        }
        return response;
    }

    @Transactional
    public void deletePortfolio(Long portfolioId) {
        log.info("Starting deletePortfolio method for portfolio ID: {}", portfolioId);
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        deletePortfolioImages(portfolio);
        weddingPlanner.setPortfolio(null);
        portfolioRepository.softDeleteById(portfolioId);

        log.info("Deleted portfolio with ID: {}", portfolioId);
    }

    public List<PortfolioDTO.Response> getAllSoftDeletedPortfolios() {
        log.info("Starting getAllSoftDeletedPortfolios method");
        return portfolioRepository.findSoftDeletedPortfolios().stream()
                .map(portfolioMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public Portfolio increaseWishListCount(Long portfolioId) {
        log.info("Starting increaseWishListCount method for portfolio ID: {}", portfolioId);
        Portfolio portfolio = getPortfolioWithLock(portfolioId);
        portfolio.increaseWishListCount();
        portfolioRepository.save(portfolio);

        log.info("Increased wishlist count for portfolio with ID: {}", portfolioId);
        return portfolio;
    }

    @Transactional
    public Portfolio decreaseWishListCount(Long portfolioId) {
        log.info("Starting decreaseWishListCount method for portfolio ID: {}", portfolioId);
        Portfolio portfolio = getPortfolioWithLock(portfolioId);
        portfolio.decreaseWishListCount();
        portfolioRepository.save(portfolio);

        log.info("Decreased wishlist count for portfolio with ID: {}", portfolioId);
        return portfolio;
    }

    @Transactional
    public Portfolio reflectNewReview(ReviewDTO.Request reviewRequest) {
        log.info("Starting reflectNewReview method for review with portfolio ID: {}", reviewRequest.getPortfolioId());
        Portfolio portfolio = portfolioRepository.findById(reviewRequest.getPortfolioId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        updatePortfolioWithNewReview(portfolio, reviewRequest);
        portfolioRepository.save(portfolio);

        log.info("Reflected new review for portfolio with ID: {}", reviewRequest.getPortfolioId());
        return portfolio;
    }

    @Transactional
    public Portfolio reflectModifiedReview(ReviewDTO.Request reviewRequest, Review existingReview) {
        log.info("Starting reflectModifiedReview method for review with portfolio ID: {}", reviewRequest.getPortfolioId());
        Portfolio portfolio = getPortfolioWithLock(reviewRequest.getPortfolioId());

        updatePortfolioWithModifiedReview(portfolio, reviewRequest, existingReview);
        portfolioRepository.save(portfolio);

        log.info("Reflected modified review for portfolio with ID: {}", reviewRequest.getPortfolioId());
        return portfolio;
    }

    public Portfolio getPortfolioByWeddingPlannerId(Long weddingPlannerId) {
        log.info("Starting getPortfolioByWeddingPlannerId method for wedding planner ID: {}", weddingPlannerId);
        return portfolioRepository.findByWeddingPlannerId(weddingPlannerId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
    }


    private void deletePortfolioImages(Portfolio portfolio) {
        String profileImageUrl = portfolio.getProfileImageUrl();
        List<String> weddingPhotoUrls = portfolio.getWeddingPhotoUrls();

        if (profileImageUrl != null) {
            s3Uploader.deleteFile(profileImageUrl);
        }
        if (weddingPhotoUrls != null) {
            weddingPhotoUrls.forEach(s3Uploader::deleteFile);
        }
    }

    private Portfolio getPortfolioWithLock(Long portfolioId) {
        return portfolioRepository.findPortfolioByIdWithPessimisticLock(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
    }

    private void updatePortfolioWithNewReview(Portfolio portfolio, ReviewDTO.Request reviewRequest) {
        portfolio.accumulateRatingSum(reviewRequest.getRating());
        portfolio.accumulateEstimate(reviewRequest.getEstimate());
        portfolio.updateMinEstimate(reviewRequest.getEstimate());
        portfolio.accumulateRadarSum(reviewRequest.getRadar());

        portfolio.increaseRatingCount(reviewRequest.getRating());
        portfolio.increaseEstimateCount(reviewRequest.getEstimate());
        portfolio.increaseRadarCount(reviewRequest.getRadar());
    }

    private void updatePortfolioWithModifiedReview(Portfolio portfolio, ReviewDTO.Request reviewRequest, Review existingReview) {
        portfolio.reduceRatingSum(existingReview.getRating());
        portfolio.reduceEstimateSum(existingReview.getEstimate());
        portfolio.reduceRadarSum(existingReview.getRadar());

        portfolio.accumulateRatingSum(reviewRequest.getRating());
        portfolio.accumulateEstimate(reviewRequest.getEstimate());
        portfolio.accumulateRadarSum(reviewRequest.getRadar());
    }

    private ReviewDTO.Response mapToReviewResponse(Review review) {
        return ReviewDTO.Response.builder()
                .id(review.getId())
                .reviewerName(review.getReviewerName())
                .portfolioId(review.getPortfolio().getId())
                .rating(review.getRating())
                .estimate(review.getEstimate())
                .radar(review.getRadar())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    public float calculateAvgRating(PortfolioDTO.Response portfolioResponse) {
        int ratingCount = portfolioResponse.getRatingCount() != null ? portfolioResponse.getRatingCount() : 0;
        return ratingCount != 0 ? Math.round((float) portfolioResponse.getRatingSum() / ratingCount * 10) / 10f : 0;
    }

    private Integer calculateAvgEstimate(PortfolioDTO.Response portfolioResponse) {
        int estimateCount = portfolioResponse.getEstimateCount() != null ? portfolioResponse.getEstimateCount() : 0;
        return estimateCount != 0 ? Math.round((float) portfolioResponse.getEstimateSum() / estimateCount / 1000) * 1000 : 0;
    }

    private Map<RadarKey, Float> calculateAvgRadar(PortfolioDTO.Response portfolioResponse) {
        int radarCount = portfolioResponse.getRadarCount() != null ? portfolioResponse.getRadarCount() : 0;
        if (radarCount == 0) {
            return Map.of(
                    RadarKey.COMMUNICATION, 0f,
                    RadarKey.BUDGET_COMPLIANCE, 0f,
                    RadarKey.PERSONAL_CUSTOMIZATION, 0f,
                    RadarKey.PRICE_RATIONALITY, 0f,
                    RadarKey.SCHEDULE_COMPLIANCE, 0f
            );
        }

        Map<RadarKey, Float> radarSum = portfolioResponse.getRadarSum();
        return radarSum.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Math.round(entry.getValue() / radarCount * 10) / 10f));
    }

    @Transactional
    public Portfolio increaseViewCount(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findPortfolioByIdWithPessimisticLock(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        portfolioRepository.increaseViewCount(portfolioId);
        portfolioRepository.save(portfolio);
        return portfolio;
    }

    public List<PortfolioOverviewDTO.Response> getTop5Portfolios() {

        return portfolioRepository.findTop5ByOrderByViewCountDesc().stream()
                .map(portfolioMapper::entityToOverviewResponse)
                .collect(Collectors.toList());
    }

    public PortfolioDTO.Response getMyPortfolio() {
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();
        Portfolio portfolio = weddingPlanner.getPortfolio();
        if (portfolio == null) {
            throw new RuntimeException("Portfolio not found");
        }
        PortfolioDTO.Response portfolioResponse = portfolioMapper.entityToResponse(portfolio);
        return portfolioResponse;
    }
}

