package com.example.demo.portfolio.service;

import com.example.demo.config.S3Uploader;
import com.example.demo.enums.review.RadarKey;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.mapper.WeddingPlannerMapper;
import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.portfolio.mapper.PortfolioMapper;
import com.example.demo.portfolio.repository.PortfolioRepository;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioDTO;
import com.example.demo.review.domain.Review;
import com.example.demo.review.dto.ReviewDTO;
import com.example.demo.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PortfolioSearchService portfolioSearchService;
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

        updatePortfolioImageUrls(portfolio);
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
        return reviewRepository.findByPortfolioId(portfolioId).stream()
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PortfolioDTO.Response createPortfolio(PortfolioDTO.Request portfolioRequest) {
        log.info("Starting createPortfolio method with data: {}", portfolioRequest);
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();

        preparePortfolioImages(portfolioRequest);

        Portfolio savedPortfolio = portfolioRepository.save(portfolioMapper.requestToEntity(portfolioRequest));
        weddingPlanner.setPortfolio(savedPortfolio);

        PortfolioDTO.Response response = buildPortfolioResponse(savedPortfolio);
        portfolioSearchService.indexDocumentUsingDTO(savedPortfolio);

        log.info("Created new portfolio with data: {}", portfolioRequest);
        return response;
    }

    @Transactional
    public PortfolioDTO.Response updatePortfolio(Long portfolioId, PortfolioDTO.Request portfolioRequest) {
        log.info("Starting updatePortfolio method for portfolio ID: {} with data: {}", portfolioId, portfolioRequest);
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();
        Portfolio existingPortfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        updatePortfolioImages(portfolioRequest, existingPortfolio);
        Portfolio updatedPortfolio = portfolioMapper.updateFromRequest(portfolioRequest, existingPortfolio);
        portfolioRepository.save(updatedPortfolio);
        weddingPlanner.setPortfolio(updatedPortfolio);

        PortfolioDTO.Response response = buildPortfolioResponse(updatedPortfolio);
        portfolioSearchService.updateDocumentUsingDTO(updatedPortfolio);

        log.info("Updated portfolio with ID: {} with data: {}", portfolioId, portfolioRequest);
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
        portfolioSearchService.deleteDocumentById(portfolioId);

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
        portfolioSearchService.updateDocumentUsingDTO(portfolio);

        log.info("Increased wishlist count for portfolio with ID: {}", portfolioId);
        return portfolio;
    }

    @Transactional
    public Portfolio decreaseWishListCount(Long portfolioId) {
        log.info("Starting decreaseWishListCount method for portfolio ID: {}", portfolioId);
        Portfolio portfolio = getPortfolioWithLock(portfolioId);
        portfolio.decreaseWishListCount();
        portfolioRepository.save(portfolio);
        portfolioSearchService.updateDocumentUsingDTO(portfolio);

        log.info("Decreased wishlist count for portfolio with ID: {}", portfolioId);
        return portfolio;
    }

    @Transactional
    public Portfolio reflectNewReview(ReviewDTO.Request reviewRequest) {
        log.info("Starting reflectNewReview method for review with portfolio ID: {}", reviewRequest.getPortfolioId());
        Portfolio portfolio = getPortfolioWithLock(reviewRequest.getPortfolioId());

        updatePortfolioWithNewReview(portfolio, reviewRequest);
        portfolioRepository.save(portfolio);
        portfolioSearchService.updateDocumentUsingDTO(portfolio);

        log.info("Reflected new review for portfolio with ID: {}", reviewRequest.getPortfolioId());
        return portfolio;
    }

    @Transactional
    public Portfolio reflectModifiedReview(ReviewDTO.Request reviewRequest, Review existingReview) {
        log.info("Starting reflectModifiedReview method for review with portfolio ID: {}", reviewRequest.getPortfolioId());
        Portfolio portfolio = getPortfolioWithLock(reviewRequest.getPortfolioId());

        updatePortfolioWithModifiedReview(portfolio, reviewRequest, existingReview);
        portfolioRepository.save(portfolio);
        portfolioSearchService.updateDocumentUsingDTO(portfolio);

        log.info("Reflected modified review for portfolio with ID: {}", reviewRequest.getPortfolioId());
        return portfolio;
    }

    public Portfolio getPortfolioByWeddingPlannerId(Long weddingPlannerId) {
        log.info("Starting getPortfolioByWeddingPlannerId method for wedding planner ID: {}", weddingPlannerId);
        return portfolioRepository.findByWeddingPlannerId(weddingPlannerId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
    }

    private void updatePortfolioImageUrls(Portfolio portfolio) {
        String cloudFrontImageUrl = s3Uploader.getImageUrl(portfolio.getProfileImageUrl());
        portfolio.setProfileImageUrl(cloudFrontImageUrl);
    }

    private void preparePortfolioImages(PortfolioDTO.Request portfolioRequest) {
        portfolioRequest.setProfileImageUrl(s3Uploader.getUniqueFilename(portfolioRequest.getProfileImageUrl()));
        portfolioRequest.setWeddingPhotoUrls(portfolioRequest.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getUniqueFilename)
                .collect(Collectors.toList()));

        s3Uploader.uploadFile(portfolioRequest.getProfileImageUrl());
        s3Uploader.uploadFileList(portfolioRequest.getWeddingPhotoUrls());
    }

    private void updatePortfolioImages(PortfolioDTO.Request portfolioRequest, Portfolio existingPortfolio) {
        if (portfolioRequest.getProfileImageUrl() != null) {
            s3Uploader.deleteFile(existingPortfolio.getProfileImageUrl());
            existingPortfolio.setProfileImageUrl(s3Uploader.getUniqueFilename(portfolioRequest.getProfileImageUrl()));
            s3Uploader.uploadFile(portfolioRequest.getProfileImageUrl());
        }
        if (portfolioRequest.getWeddingPhotoUrls() != null) {
            existingPortfolio.getWeddingPhotoUrls().forEach(s3Uploader::deleteFile);
            existingPortfolio.setWeddingPhotoUrls(portfolioRequest.getWeddingPhotoUrls().stream()
                    .map(s3Uploader::getUniqueFilename)
                    .collect(Collectors.toList()));
            s3Uploader.uploadFileList(portfolioRequest.getWeddingPhotoUrls());
        }
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

    private PortfolioDTO.Response buildPortfolioResponse(Portfolio portfolio) {
        PortfolioDTO.Response response = portfolioMapper.entityToResponse(portfolio);
        response.setPresignedProfileImageUrl(s3Uploader.getImageUrl(portfolio.getProfileImageUrl()));
        response.setPresignedWeddingPhotoUrls(portfolio.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getImageUrl)
                .collect(Collectors.toList()));
        return response;
    }

    private ReviewDTO.Response mapToReviewResponse(Review review) {
        return ReviewDTO.Response.builder()
                .id(review.getId())
                .portfolioId(review.getPortfolio().getId())
                .rating(review.getRating())
                .estimate(review.getEstimate())
                .radar(review.getRadar())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    private float calculateAvgRating(PortfolioDTO.Response portfolioResponse) {
        int ratingCount = portfolioResponse.getRatingCount() != null ? portfolioResponse.getRatingCount() : 0;
        return ratingCount != 0 ? portfolioResponse.getRatingSum() / ratingCount : 0f;
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
}
