package com.example.demo.portfolio.service;

import com.example.demo.config.S3Uploader;
import com.example.demo.enums.review.RadarKey;
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

    public List<PortfolioDTO.Response> getAllPortfolios() {
        return portfolioRepository.findAll().stream()
                .map(portfolioMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public PortfolioDTO.Response getPortfolioById(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        String CloudFrontImageUrl = s3Uploader.getImageUrl(portfolio.getProfileImageUrl());
        portfolio.setProfileImageUrl(CloudFrontImageUrl);
        return portfolioMapper.entityToResponse(portfolio);
    }

    @Transactional
    public PortfolioDTO.Response createPortfolio(PortfolioDTO.Request portfolioRequest) {
        //Change image name to be unique
        portfolioRequest.setProfileImageUrl(s3Uploader.getUniqueFilename(portfolioRequest.getProfileImageUrl()));
        portfolioRequest.setWeddingPhotoUrls(portfolioRequest.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getUniqueFilename)
                .collect(Collectors.toList()));

        //Upload image to s3
        String presignedUrl = s3Uploader.uploadFile(portfolioRequest.getProfileImageUrl());
        List<String> presignedUrlList = s3Uploader.uploadFileList(portfolioRequest.getWeddingPhotoUrls());
        //save portfolio, set presigned url and cloudfront url to response
        Portfolio portfolio = portfolioMapper.requestToEntity(portfolioRequest);
        portfolio = portfolioRepository.save(portfolio);
        PortfolioDTO.Response response = portfolioMapper.entityToResponse(portfolio);
        response.setPresignedProfileImageUrl(presignedUrl);
        response.setPresignedWeddingPhotoUrls(presignedUrlList);

        response.setProfileImageUrl(s3Uploader.getImageUrl(portfolio.getProfileImageUrl()));
        response.setWeddingPhotoUrls(portfolio.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getImageUrl)
                .collect(Collectors.toList()));

        return response;
    }

    @Transactional
    public PortfolioDTO.Response updatePortfolio(Long id, PortfolioDTO.Request portfolioRequest) {
        Portfolio existingPortfolio = portfolioRepository.findById(id)
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
        PortfolioDTO.Response response = portfolioMapper.entityToResponse(savedPortfolio);
        response.setPresignedProfileImageUrl(updatedPortfolio.getProfileImageUrl());
        response.setPresignedWeddingPhotoUrls(updatedPortfolio.getWeddingPhotoUrls());

        response.setProfileImageUrl(s3Uploader.getImageUrl(updatedPortfolio.getProfileImageUrl()));
        response.setWeddingPhotoUrls(updatedPortfolio.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getImageUrl)
                .collect(Collectors.toList()));

        return portfolioMapper.entityToResponse(savedPortfolio);
    }

    @Transactional
    public void deletePortfolio(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        String profileImageUrl = portfolio.getProfileImageUrl();
        List<String> weddingPhotoUrls = portfolio.getWeddingPhotoUrls();

        if (profileImageUrl != null) {
            s3Uploader.deleteFile(portfolio.getProfileImageUrl());
        }
        if (weddingPhotoUrls != null) {
            weddingPhotoUrls.forEach(s3Uploader::deleteFile);
        }

        portfolioRepository.softDeleteById(id);
    }

    public List<PortfolioDTO.Response> getAllSoftDeletedPortfolios() {
        return portfolioRepository.findSoftDeletedPortfolios().stream()
                .map(portfolioMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public Portfolio increaseWishListCount(Long id) {
        Portfolio portfolio = portfolioRepository.findPortfolioByIdWithPessimisticLock(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        portfolio.increaseWishListCount();
        portfolioRepository.save(portfolio);
        return portfolio;
    }

    @Transactional
    public Portfolio decreaseListCount(Long id) {
        Portfolio portfolio = portfolioRepository.findPortfolioByIdWithPessimisticLock(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        portfolio.decreaseWishListCount();
        portfolioRepository.save(portfolio);
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

        return portfolio;

    }


}
