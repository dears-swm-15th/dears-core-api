package com.example.demo.review.service;

import com.example.demo.config.S3Uploader;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.service.PortfolioService;
import com.example.demo.review.domain.Review;
import com.example.demo.review.dto.ReviewDTO;
import com.example.demo.review.mapper.ReviewMapper;
import com.example.demo.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper = ReviewMapper.INSTANCE;

    private final PortfolioService portfolioService;

    private final S3Uploader s3Uploader;

    private final CustomUserDetailsService customUserDetailsService;

    public List<ReviewDTO.Response> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public ReviewDTO.Response getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        List<String> CloudFrontImageUrl = s3Uploader.getImageUrls(review.getWeddingPhotoUrls());
        review.setWeddingPhotoUrls(CloudFrontImageUrl);

        return reviewMapper.entityToResponse(review);
    }

    @Transactional
    public ReviewDTO.Response createReviewForWeddingPlanner(ReviewDTO.Request reviewRequest) {
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner().orElseThrow();

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

    @Transactional
    public ReviewDTO.Response createReviewForCustomer(ReviewDTO.Request reviewRequest) {
        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer().orElseThrow();

        //Change image name to be unique
        reviewRequest.setWeddingPhotoUrls(reviewRequest.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getUniqueFilename)
                .collect(Collectors.toList()));

        //Upload image to s3
        List<String> presignedUrlList = s3Uploader.uploadFileList(reviewRequest.getWeddingPhotoUrls());

        //save preview, set presigned url and cloudfront url to response
        Review review = reviewMapper.requestToEntity(reviewRequest);
        Portfolio portfolio = portfolioService.reflectNewReview(reviewRequest);
        review.setPortfolio(portfolio);
        review.setReviewerId(customer.getId());
        review.setIsProvided(false);

        ReviewDTO.Response response = reviewMapper.entityToResponse(review);

        response.setPresignedWeddingPhotoUrls(presignedUrlList);

        response.setWeddingPhotoUrls(review.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getImageUrl)
                .collect(Collectors.toList()));

        reviewRepository.save(review);

        return response;
    }

    @Transactional
    public ReviewDTO.Response modifyReviewForWeddingPlanner(Long id, ReviewDTO.Request reviewRequest) {
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner().orElseThrow();

        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (existingReview.getReviewerId() != weddingPlanner.getId()) {
            throw new RuntimeException("권한이 없습니다.");
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

    @Transactional
    public ReviewDTO.Response modifyReviewForCustomer(Long id, ReviewDTO.Request reviewRequest) {
        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer().orElseThrow();

        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (existingReview.getReviewerId() != customer.getId()) {
            throw new RuntimeException("권한이 없습니다.");
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


    @Transactional
    public void deleteReviewForWeddingPlanner(Long id) {
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner().orElseThrow();

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (review.getReviewerId() != weddingPlanner.getId()) {
            throw new RuntimeException("권한이 없습니다.");
        }
        List<String> weddingPhotoUrls = review.getWeddingPhotoUrls();

        if (weddingPhotoUrls != null) {
            s3Uploader.deleteFiles(weddingPhotoUrls);
        }

        reviewRepository.softDeleteById(id);
    }

    @Transactional
    public void deleteReviewForCustomer(Long id) {
        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer().orElseThrow();

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (review.getReviewerId() != customer.getId()) {
            throw new RuntimeException("권한이 없습니다.");
        }
        List<String> weddingPhotoUrls = review.getWeddingPhotoUrls();

        if (weddingPhotoUrls != null) {
            s3Uploader.deleteFiles(weddingPhotoUrls);
        }

        reviewRepository.softDeleteById(id);
    }

    public List<ReviewDTO.Response> getAllSoftDeletedReviews() {
        return reviewRepository.findSoftDeletedReviews().stream()
                .map(reviewMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO.Response> getMyReviewsForCustomer() throws UsernameNotFoundException {
        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer().orElseThrow();
        return reviewRepository.findReviewsForCustomer(customer.getId()).stream()
                .map(reviewMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO.Response> getMyReviewsForWeddingplanner() throws UsernameNotFoundException {
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner().orElseThrow();

        return reviewRepository.findReviewsForWeddingPlanner(weddingPlanner.getId()).stream()
                        .map(reviewMapper::entityToResponse)
                        .collect(Collectors.toList());
    }

}
