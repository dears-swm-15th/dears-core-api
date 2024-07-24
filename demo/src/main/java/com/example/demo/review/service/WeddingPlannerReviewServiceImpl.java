package com.example.demo.review.service;

import com.example.demo.config.S3Uploader;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.service.PortfolioService;
import com.example.demo.review.domain.Review;
import com.example.demo.review.dto.ReviewDTO;
import com.example.demo.review.mapper.ReviewMapper;
import com.example.demo.review.repository.ReviewRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeddingPlannerReviewServiceImpl extends ReviewService{

    protected final ReviewRepository reviewRepository;
    protected final ReviewMapper reviewMapper = ReviewMapper.INSTANCE;
    protected final PortfolioService portfolioService;
    protected final S3Uploader s3Uploader;
    protected final CustomUserDetailsService customUserDetailsService;

    public WeddingPlannerReviewServiceImpl(ReviewRepository reviewRepository, PortfolioService portfolioService, S3Uploader s3Uploader, CustomUserDetailsService customUserDetailsService, ReviewRepository reviewRepository1, PortfolioService portfolioService1, S3Uploader s3Uploader1, CustomUserDetailsService customUserDetailsService1) {
        super(reviewRepository, portfolioService, s3Uploader, customUserDetailsService);
        this.reviewRepository = reviewRepository1;
        this.portfolioService = portfolioService1;
        this.s3Uploader = s3Uploader1;
        this.customUserDetailsService = customUserDetailsService1;
    }

    @Override
    @Transactional
    public ReviewDTO.Response createReview(ReviewDTO.Request reviewRequest) {
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();

        reviewRequest.setWeddingPhotoUrls(reviewRequest.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getUniqueFilename)
                .collect(Collectors.toList()));

        //Upload image to s3
        List<String> presignedUrlList = s3Uploader.uploadFileList(reviewRequest.getWeddingPhotoUrls());

        //save preview, set presigned url and cloudfront url to response
        Review review = reviewMapper.requestToEntity(reviewRequest);
        Portfolio portfolio = portfolioService.reflectNewReview(reviewRequest);
        review.setPortfolio(portfolio);
        review.setReviewerId(weddingPlanner.getId());
        review.setIsProvided(true);

        ReviewDTO.Response response = reviewMapper.entityToResponse(review);

        response.setPresignedWeddingPhotoUrls(presignedUrlList);

        response.setWeddingPhotoUrls(review.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getImageUrl)
                .collect(Collectors.toList()));

        reviewRepository.save(review);

        return response;
    }

    @Override
    @Transactional
    public ReviewDTO.Response modifyReview(Long id, ReviewDTO.Request reviewRequest) {
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();

        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (existingReview.getReviewerId() != weddingPlanner.getId()) {
            throw new RuntimeException("Not authorized to modify this review");
        }
        List<String> weddingPhotoUrls = existingReview.getWeddingPhotoUrls();
        if (weddingPhotoUrls != null) {
            //Delete existing images from s3 and upload new images
            weddingPhotoUrls.forEach(s3Uploader::deleteFile);
            existingReview.setWeddingPhotoUrls(reviewRequest.getWeddingPhotoUrls().stream()
                    .map(s3Uploader::getUniqueFilename)
                    .collect(Collectors.toList()));
            s3Uploader.uploadFileList(reviewRequest.getWeddingPhotoUrls());
        }

        //save review, set presigned url and cloudfront url to response
        Review updatedReview = reviewMapper.updateFromRequest(reviewRequest, existingReview);
        Portfolio portfolio = portfolioService.reflectModifiedReview(reviewRequest, existingReview);
        updatedReview.setPortfolio(portfolio);

        ReviewDTO.Response response = reviewMapper.entityToResponse(updatedReview);
        response.setPresignedWeddingPhotoUrls(updatedReview.getWeddingPhotoUrls());

        response.setWeddingPhotoUrls(updatedReview.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getImageUrl)
                .collect(Collectors.toList()));

        reviewRepository.save(updatedReview);

        return reviewMapper.entityToResponse(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (review.getReviewerId() != weddingPlanner.getId()) {
            throw new RuntimeException("Not authorized to delete this review");
        }
        List<String> weddingPhotoUrls = review.getWeddingPhotoUrls();

        if (weddingPhotoUrls != null) {
            s3Uploader.deleteFiles(weddingPhotoUrls);
        }

        reviewRepository.softDeleteById(id);
    }

    @Override
    public List<ReviewDTO.Response> getMyReviews() throws UsernameNotFoundException {
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();

        return reviewRepository.findReviewsForWeddingPlanner(weddingPlanner.getId()).stream()
                .map(reviewMapper::entityToResponse)
                .collect(Collectors.toList());
    }
}
