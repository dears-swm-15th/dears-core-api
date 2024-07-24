package com.example.demo.review.service;

import com.example.demo.config.S3Uploader;
import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.portfolio.service.PortfolioService;
import com.example.demo.review.domain.Review;
import com.example.demo.review.dto.ReviewDTO;
import com.example.demo.review.mapper.ReviewMapper;
import com.example.demo.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("reviewService")
@RequiredArgsConstructor
public abstract class ReviewService {


    protected final ReviewRepository reviewRepository;
    protected final ReviewMapper reviewMapper = ReviewMapper.INSTANCE;
    protected final PortfolioService portfolioService;
    protected final S3Uploader s3Uploader;
    protected final CustomUserDetailsService customUserDetailsService;


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

    public List<ReviewDTO.Response> getAllSoftDeletedReviews(){
        return reviewRepository.findSoftDeletedReviews().stream()
                .map(reviewMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    abstract ReviewDTO.Response createReview(ReviewDTO.Request reviewRequest);
    abstract ReviewDTO.Response modifyReview(Long id, ReviewDTO.Request reviewRequest);
    abstract void deleteReview(Long id);
    abstract List<ReviewDTO.Response> getMyReviews() throws UsernameNotFoundException;

}
