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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper = ReviewMapper.INSTANCE;

    private final PortfolioService portfolioService;

    private final S3Uploader s3Uploader;

    private final CustomUserDetailsService customUserDetailsService;

    public List<ReviewDTO.Response> getAllReviews() {
        log.info("Fetching all reviews");
        return reviewRepository.findAll().stream()
                .map(reviewMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public ReviewDTO.Response getReviewById(Long reviewId) {
        log.info("Fetching review with ID: {}", reviewId);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        List<String> cloudFrontImageUrls = s3Uploader.getImageUrls(review.getWeddingPhotoUrls());
        review.setWeddingPhotoUrls(cloudFrontImageUrls);

        return reviewMapper.entityToResponse(review);
    }

    @Transactional
    public ReviewDTO.Response createReviewForWeddingPlanner(ReviewDTO.Request reviewRequest) {
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();
        log.info("Creating review for wedding planner with data: {}", reviewRequest);

        reviewRequest.setWeddingPhotoUrls(reviewRequest.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getUniqueFilename)
                .collect(Collectors.toList()));

        List<String> presignedUrlList = s3Uploader.uploadFileList(reviewRequest.getWeddingPhotoUrls());

        Review review = reviewMapper.requestToEntity(reviewRequest);
        Portfolio portfolio = portfolioService.reflectNewReview(reviewRequest);
        review.setPortfolio(portfolio);
        review.setReviewerId(weddingPlanner.getId());
        review.setIsProvided(true);

        reviewRepository.save(review);

        ReviewDTO.Response response = reviewMapper.entityToResponse(review);
        response.setPresignedWeddingPhotoUrls(presignedUrlList);
        response.setWeddingPhotoUrls(review.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getImageUrl)
                .collect(Collectors.toList()));

        log.info("Successfully created review for wedding planner with ID: {}", review.getId());
        return response;
    }

    @Transactional
    public ReviewDTO.Response createReviewForCustomer(ReviewDTO.Request reviewRequest) {
        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer();
        log.info("Creating review for customer with data: {}", reviewRequest);

        reviewRequest.setWeddingPhotoUrls(reviewRequest.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getUniqueFilename)
                .collect(Collectors.toList()));

        List<String> presignedUrlList = s3Uploader.uploadFileList(reviewRequest.getWeddingPhotoUrls());

        Review review = reviewMapper.requestToEntity(reviewRequest);
        Portfolio portfolio = portfolioService.reflectNewReview(reviewRequest);
        review.setPortfolio(portfolio);
        review.setReviewerId(customer.getId());
        review.setIsProvided(false);

        reviewRepository.save(review);

        ReviewDTO.Response response = reviewMapper.entityToResponse(review);
        response.setPresignedWeddingPhotoUrls(presignedUrlList);
        response.setWeddingPhotoUrls(review.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getImageUrl)
                .collect(Collectors.toList()));

        log.info("Successfully created review for customer with ID: {}", review.getId());
        return response;
    }

    @Transactional
    public ReviewDTO.Response modifyReviewForWeddingPlanner(Long reviewId, ReviewDTO.Request reviewRequest) {
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();
        log.info("Modifying review for wedding planner with review ID: {} and data: {}", reviewId, reviewRequest);

        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!existingReview.getReviewerId().equals(weddingPlanner.getId())) {
            log.error("Unauthorized attempt to modify review with ID: {}", reviewId);
            throw new RuntimeException("Not authorized to modify this review");
        }

        List<String> weddingPhotoUrls = existingReview.getWeddingPhotoUrls();
        if (weddingPhotoUrls != null) {
            weddingPhotoUrls.forEach(s3Uploader::deleteFile);
            existingReview.setWeddingPhotoUrls(reviewRequest.getWeddingPhotoUrls().stream()
                    .map(s3Uploader::getUniqueFilename)
                    .collect(Collectors.toList()));
            s3Uploader.uploadFileList(reviewRequest.getWeddingPhotoUrls());
        }

        Review updatedReview = reviewMapper.updateFromRequest(reviewRequest, existingReview);
        Portfolio portfolio = portfolioService.reflectModifiedReview(reviewRequest, existingReview);
        updatedReview.setPortfolio(portfolio);

        reviewRepository.save(updatedReview);

        ReviewDTO.Response response = reviewMapper.entityToResponse(updatedReview);
        response.setPresignedWeddingPhotoUrls(updatedReview.getWeddingPhotoUrls());
        response.setWeddingPhotoUrls(updatedReview.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getImageUrl)
                .collect(Collectors.toList()));

        log.info("Successfully modified review for wedding planner with ID: {}", reviewId);
        return response;
    }

    @Transactional
    public ReviewDTO.Response modifyReviewForCustomer(Long reviewId, ReviewDTO.Request reviewRequest) {
        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer();
        log.info("Modifying review for customer with review ID: {} and data: {}", reviewId, reviewRequest);

        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!existingReview.getReviewerId().equals(customer.getId())) {
            log.error("Unauthorized attempt to modify review with ID: {}", reviewId);
            throw new RuntimeException("Not authorized to modify this review");
        }

        List<String> weddingPhotoUrls = existingReview.getWeddingPhotoUrls();
        if (weddingPhotoUrls != null) {
            weddingPhotoUrls.forEach(s3Uploader::deleteFile);
            existingReview.setWeddingPhotoUrls(reviewRequest.getWeddingPhotoUrls().stream()
                    .map(s3Uploader::getUniqueFilename)
                    .collect(Collectors.toList()));
            s3Uploader.uploadFileList(reviewRequest.getWeddingPhotoUrls());
        }

        Review updatedReview = reviewMapper.updateFromRequest(reviewRequest, existingReview);
        Portfolio portfolio = portfolioService.reflectModifiedReview(reviewRequest, existingReview);
        updatedReview.setPortfolio(portfolio);

        reviewRepository.save(updatedReview);

        ReviewDTO.Response response = reviewMapper.entityToResponse(updatedReview);
        response.setPresignedWeddingPhotoUrls(updatedReview.getWeddingPhotoUrls());
        response.setWeddingPhotoUrls(updatedReview.getWeddingPhotoUrls().stream()
                .map(s3Uploader::getImageUrl)
                .collect(Collectors.toList()));

        log.info("Successfully modified review for customer with ID: {}", reviewId);
        return response;
    }

    @Transactional
    public void deleteReviewForWeddingPlanner(Long reviewId) {
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();
        log.info("Deleting review for wedding planner with review ID: {}", reviewId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getReviewerId().equals(weddingPlanner.getId())) {
            log.error("Unauthorized attempt to delete review with ID: {}", reviewId);
            throw new RuntimeException("Not authorized to delete this review");
        }

        List<String> weddingPhotoUrls = review.getWeddingPhotoUrls();
        if (weddingPhotoUrls != null) {
            s3Uploader.deleteFiles(weddingPhotoUrls);
        }

        reviewRepository.softDeleteById(reviewId);
        log.info("Successfully deleted review for wedding planner with ID: {}", reviewId);
    }

    @Transactional
    public void deleteReviewForCustomer(Long reviewId) {
        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer();
        log.info("Deleting review for customer with review ID: {}", reviewId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getReviewerId().equals(customer.getId())) {
            log.error("Unauthorized attempt to delete review with ID: {}", reviewId);
            throw new RuntimeException("Not authorized to delete this review");
        }

        List<String> weddingPhotoUrls = review.getWeddingPhotoUrls();
        if (weddingPhotoUrls != null) {
            s3Uploader.deleteFiles(weddingPhotoUrls);
        }

        reviewRepository.softDeleteById(reviewId);
        log.info("Successfully deleted review for customer with ID: {}", reviewId);
    }

    public List<ReviewDTO.Response> getAllSoftDeletedReviews() {
        log.info("Fetching all soft-deleted reviews");
        return reviewRepository.findSoftDeletedReviews().stream()
                .map(reviewMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO.Response> getMyReviewsForCustomer() throws UsernameNotFoundException {
        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer();
        log.info("Fetching reviews for customer with ID: {}", customer.getId());
        return reviewRepository.findReviewsForCustomer(customer.getId()).stream()
                .map(reviewMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO.Response> getMyReviewsForWeddingplanner() throws UsernameNotFoundException {
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();
        log.info("Fetching reviews for wedding planner with ID: {}", weddingPlanner.getId());
        return reviewRepository.findReviewsForWeddingPlanner(weddingPlanner.getId()).stream()
                .map(reviewMapper::entityToResponse)
                .collect(Collectors.toList());
    }
}
