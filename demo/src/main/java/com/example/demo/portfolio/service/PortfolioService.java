package com.example.demo.portfolio.service;

import com.example.demo.config.S3Uploader;
import com.example.demo.enums.review.RadarKey;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.portfolio.mapper.PortfolioMapper;
import com.example.demo.portfolio.repository.PortfolioRepository;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioDTO;
import com.example.demo.review.domain.Review;
import com.example.demo.review.dto.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper = PortfolioMapper.INSTANCE;

    private final S3Uploader s3Uploader;

    private final PortfolioSearchService portfolioSearchService;

    private final CustomUserDetailsService customUserDetailsService;

    public List<PortfolioDTO.Response> getAllPortfolios() {
        return portfolioRepository.findAll().stream()
                .map(portfolioMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public PortfolioDTO.Response getPortfolioById(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        String CloudFrontImageUrl = s3Uploader.getImageUrl(portfolio.getProfileImageUrl());
        portfolio.setProfileImageUrl(CloudFrontImageUrl);
        PortfolioDTO.Response portfolioResponse =  portfolioMapper.entityToResponse(portfolio);

        portfolioResponse.setAvgRating(calculateAvgRating(portfolioResponse));
        portfolioResponse.setAvgEstimate(calculateAvgEstimate(portfolioResponse));
        portfolioResponse.setAvgRadar(calculateAvgRadar(portfolioResponse));

        return portfolioResponse;
    }

    @Transactional
    public PortfolioDTO.Response createPortfolio(PortfolioDTO.Request portfolioRequest) {

        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();

        //Change image name to be unique
        portfolioRequest.setProfileImageUrl(s3Uploader.getUniqueFilename(portfolioRequest.getProfileImageUrl()));
        portfolioRequest.setWeddingPhotoUrls(portfolioRequest.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getUniqueFilename)
                .collect(Collectors.toList()));

        //Upload image to s3
        String presignedUrl = s3Uploader.uploadFile(portfolioRequest.getProfileImageUrl());
        List<String> presignedUrlList = s3Uploader.uploadFileList(portfolioRequest.getWeddingPhotoUrls());
        //save portfolio, set presigned url and cloudfront url to response
        Portfolio savedPortfolio = portfolioMapper.requestToEntity(portfolioRequest);
        savedPortfolio = portfolioRepository.save(savedPortfolio);
        weddingPlanner.setPortfolio(savedPortfolio);

        PortfolioDTO.Response response = portfolioMapper.entityToResponse(savedPortfolio);
        response.setPresignedProfileImageUrl(presignedUrl);
        response.setPresignedWeddingPhotoUrls(presignedUrlList);

        response.setProfileImageUrl(s3Uploader.getImageUrl(savedPortfolio.getProfileImageUrl()));
        response.setWeddingPhotoUrls(savedPortfolio.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getImageUrl)
                .collect(Collectors.toList()));
        portfolioSearchService.indexDocumentUsingDTO(savedPortfolio);
        return response;
    }

    @Transactional
    public PortfolioDTO.Response updatePortfolio(Long portfolioId, PortfolioDTO.Request portfolioRequest) {

        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();

        Portfolio existingPortfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        if(portfolioRequest.getProfileImageUrl() != null) {
            //Delete existing image from s3 and upload new image
            s3Uploader.deleteFile(existingPortfolio.getProfileImageUrl());
            existingPortfolio.setProfileImageUrl(s3Uploader.getUniqueFilename(portfolioRequest.getProfileImageUrl()));
            s3Uploader.uploadFile(portfolioRequest.getProfileImageUrl());
        }
        if (portfolioRequest.getWeddingPhotoUrls() != null) {
            //Delete existing images from s3 and upload new images
            existingPortfolio.getWeddingPhotoUrls().forEach(s3Uploader::deleteFile);
            existingPortfolio.setWeddingPhotoUrls(portfolioRequest.getWeddingPhotoUrls().stream()
                    .map(s3Uploader::getUniqueFilename)
                    .collect(Collectors.toList()));
            s3Uploader.uploadFileList(portfolioRequest.getWeddingPhotoUrls());
        }
        //save portfolio, set presigned url and cloudfront url to response
        Portfolio updatedPortfolio = portfolioMapper.updateFromRequest(portfolioRequest, existingPortfolio);

        Portfolio savedPortfolio = portfolioRepository.save(updatedPortfolio);
        weddingPlanner.setPortfolio(savedPortfolio);
        PortfolioDTO.Response response = portfolioMapper.entityToResponse(savedPortfolio);
        response.setPresignedProfileImageUrl(updatedPortfolio.getProfileImageUrl());
        response.setPresignedWeddingPhotoUrls(updatedPortfolio.getWeddingPhotoUrls());

        response.setProfileImageUrl(s3Uploader.getImageUrl(updatedPortfolio.getProfileImageUrl()));
        response.setWeddingPhotoUrls(updatedPortfolio.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getImageUrl)
                .collect(Collectors.toList()));
        portfolioSearchService.updateDocumentUsingDTO(savedPortfolio);
        return portfolioMapper.entityToResponse(savedPortfolio);
    }

    @Transactional
    public void deletePortfolio(Long portfolioId) {

        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        String profileImageUrl = portfolio.getProfileImageUrl();
        List<String> weddingPhotoUrls = portfolio.getWeddingPhotoUrls();

        if (profileImageUrl != null) {
            s3Uploader.deleteFile(portfolio.getProfileImageUrl());
        }
        if (weddingPhotoUrls != null) {
            weddingPhotoUrls.forEach(s3Uploader::deleteFile);
        }

        weddingPlanner.setPortfolio(null);

        portfolioRepository.softDeleteById(portfolioId);
        portfolioSearchService.deleteDocumentById(portfolioId); //검색으로는 나타나지 못하도록 구현
    }

    public List<PortfolioDTO.Response> getAllSoftDeletedPortfolios() {
        return portfolioRepository.findSoftDeletedPortfolios().stream()
                .map(portfolioMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public Portfolio increaseWishListCount(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findPortfolioByIdWithPessimisticLock(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        portfolio.increaseWishListCount();
        portfolioRepository.save(portfolio);
        portfolioSearchService.updateDocumentUsingDTO(portfolio);
        return portfolio;
    }

    @Transactional
    public Portfolio decreaseWishListCount(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findPortfolioByIdWithPessimisticLock(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        portfolio.decreaseWishListCount();
        portfolioRepository.save(portfolio);
        portfolioSearchService.updateDocumentUsingDTO(portfolio);
        return portfolio;
    }

    @Transactional
    public Portfolio reflectNewReview(ReviewDTO.Request reviewRequest) {
        Long portfolioId = reviewRequest.getPortfolioId();
        Portfolio portfolio = portfolioRepository.findPortfolioByIdWithPessimisticLock(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        Float rating = reviewRequest.getRating();
        Integer estimate = reviewRequest.getEstimate();
        Map<RadarKey, Float> radar = reviewRequest.getRadar();

        portfolio.accumulateRatingSum(rating);
        portfolio.accumulateEstimate(estimate);
        portfolio.updateMinEstimate(estimate);
        portfolio.accumulateRadarSum(radar);

        portfolio.increaseRatingCount(rating);
        portfolio.increaseEstimateCount(estimate);
        portfolio.increaseRadarCount(radar);

        portfolioRepository.save(portfolio);
        portfolioSearchService.updateDocumentUsingDTO(portfolio);

        return portfolio;
    }

    @Transactional
    public Portfolio reflectModifiedReview(ReviewDTO.Request reviewRequest, Review existingReview) {
        Long portfolioId = reviewRequest.getPortfolioId();
        Portfolio portfolio = portfolioRepository.findPortfolioByIdWithPessimisticLock(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        Float existingRating = existingReview.getRating();
        Integer existingEstimate = existingReview.getEstimate();
        // existingEstimate가 동시에 minEstimate이면 이 리뷰의 estimate가 수정되는 경우 문제 생김.
        Map<RadarKey, Float> existingRadar = existingReview.getRadar();

        portfolio.reduceRatingSum(existingRating);
        portfolio.reduceEstimateSum(existingEstimate);
        portfolio.reduceRadarSum(existingRadar);

        Float newRating = reviewRequest.getRating();
        Integer newEstimate = reviewRequest.getEstimate();
        // Integer minEstimate = reviewRequest.getMinEstimate();
        Map<RadarKey, Float> newRadar = reviewRequest.getRadar();

        portfolio.accumulateRatingSum(newRating);
        portfolio.accumulateEstimate(newEstimate);
        portfolio.accumulateRadarSum(newRadar);

        portfolioRepository.save(portfolio);
        portfolioSearchService.updateDocumentUsingDTO(portfolio);


        return portfolio;

    }

    public Portfolio getPortfolioByWeddingPlannerId(Long weddingPlannerId) {
        return portfolioRepository.findByWeddingPlannerId(weddingPlannerId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
    }

    private float calculateAvgRating(PortfolioDTO.Response portfolioResponse) {
        float avgRating = 0f;
        if (portfolioResponse.getRatingCount() == null) {
            portfolioResponse.setRatingCount(0);
        }
        if (portfolioResponse.getRatingCount() != 0) {
            avgRating = (float) portfolioResponse.getRatingSum() / portfolioResponse.getRatingCount();
        }
        return avgRating;
    }

    private Integer calculateAvgEstimate(PortfolioDTO.Response portfolioResponse) {
        float avgEstimate = 0f;
        Integer avgEstimateInt = 0;
        if (portfolioResponse.getEstimateCount() == null) {
            portfolioResponse.setEstimateCount(0);
        }
        if (portfolioResponse.getEstimateCount() != 0) {
            avgEstimate = (float) portfolioResponse.getEstimateSum() / portfolioResponse.getEstimateCount();
            avgEstimateInt = Math.round(avgEstimate / 1000) * 1000;
        }
        return avgEstimateInt;
    }

    private Map<RadarKey, Float> calculateAvgRadar(PortfolioDTO.Response portfolioResponse) {
        Map<RadarKey, Float> avgRadar = Map.of(
                RadarKey.COMMUNICATION, 0f,
                RadarKey.BUDGET_COMPLIANCE, 0f,
                RadarKey.PERSONAL_CUSTOMIZATION, 0f,
                RadarKey.PRICE_RATIONALITY, 0f,
                RadarKey.SCHEDULE_COMPLIANCE, 0f
        );
        if (portfolioResponse.getRadarCount() == null) {
            portfolioResponse.setRadarCount(0);
        }
        if (portfolioResponse.getRadarCount() != 0) {
            Map<RadarKey, Float> radarSum = portfolioResponse.getRadarSum();
            Integer radarCount = portfolioResponse.getRadarCount();
            // update avgRadar using radarSum and radarCount
            avgRadar = radarSum.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() / radarCount));
        }
        return avgRadar;
    }

}
