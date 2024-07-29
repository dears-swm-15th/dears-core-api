package com.example.demo.review.controller;

import com.example.demo.review.dto.ReviewDTO;
import com.example.demo.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/review")
@Tag(name = "review", description = "리뷰 API")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/shared/all")
    @Operation(summary = "[공통] 전체 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getAllReviews() {
        List<ReviewDTO.Response> reviewResponses = reviewService.getAllReviews();
        log.info("Fetched all reviews");
        return ResponseEntity.ok(reviewResponses);
    }

    @GetMapping("/shared/{reviewId}")
    @Operation(summary = "[공통] 특정 리뷰 조회")
    public ResponseEntity<ReviewDTO.Response> getReviewById(
            @Parameter(description = "reviewId")
            @PathVariable Long reviewId) {
        ReviewDTO.Response reviewResponse = reviewService.getReviewById(reviewId);
        log.info("Fetched review with ID: {}", reviewId);
        return ResponseEntity.ok(reviewResponse);
    }

    @PostMapping("/weddingplanner/create")
    @Operation(summary = "[웨딩플래너] 리뷰 작성")
    public ResponseEntity<ReviewDTO.Response> createReviewForWeddingPlanner(@RequestBody ReviewDTO.Request reviewRequest) {
        ReviewDTO.Response createdReview = reviewService.createReviewForWeddingPlanner(reviewRequest);
        log.info("Created review for wedding planner with data: {}", reviewRequest);
        return ResponseEntity.ok(createdReview);
    }

    @PostMapping("/customer/create")
    @Operation(summary = "[신랑신부] 리뷰 작성")
    public ResponseEntity<ReviewDTO.Response> createReviewForCustomer(@RequestBody ReviewDTO.Request reviewRequest) {
        ReviewDTO.Response createdReview = reviewService.createReviewForCustomer(reviewRequest);
        log.info("Created review for customer with data: {}", reviewRequest);
        return ResponseEntity.ok(createdReview);
    }

    @PostMapping("/weddingplanner/update/{reviewId}")
    @Operation(summary = "[웨딩플래너] 특정 리뷰 업데이트")
    public ResponseEntity<ReviewDTO.Response> updateReviewForWeddingPlanner(
            @Parameter(description = "reviewId")
            @PathVariable Long reviewId, @RequestBody ReviewDTO.Request reviewRequest) {
        ReviewDTO.Response updatedReview = reviewService.modifyReviewForWeddingPlanner(reviewId, reviewRequest);
        log.info("Updated review for wedding planner with ID: {} with data: {}", reviewId, reviewRequest);
        return ResponseEntity.ok(updatedReview);
    }

    @PostMapping("/customer/update/{reviewId}")
    @Operation(summary = "[신랑신부] 특정 리뷰 업데이트")
    public ResponseEntity<ReviewDTO.Response> updateReviewForCustomer(
            @Parameter(description = "reviewId")
            @PathVariable Long reviewId, @RequestBody ReviewDTO.Request reviewRequest) {
        ReviewDTO.Response updatedReview = reviewService.modifyReviewForCustomer(reviewId, reviewRequest);
        log.info("Updated review for customer with ID: {} with data: {}", reviewId, reviewRequest);
        return ResponseEntity.ok(updatedReview);
    }

    @PostMapping("/weddingplanner/delete/{reviewId}")
    @Operation(summary = "[웨딩플래너] 특정 리뷰 삭제")
    public ResponseEntity<Void> deleteReviewForWeddingPlanner(
            @Parameter(description = "reviewId")
            @PathVariable Long reviewId) {
        reviewService.deleteReviewForWeddingPlanner(reviewId);
        log.info("Deleted review for wedding planner with ID: {}", reviewId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/customer/delete/{reviewId}")
    @Operation(summary = "[신랑신부] 특정 리뷰 삭제")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "reviewId")
            @PathVariable Long reviewId) {
        reviewService.deleteReviewForCustomer(reviewId);
        log.info("Deleted review for customer with ID: {}", reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shared/soft-deleted")
    @Operation(summary = "[공통] soft-deleted 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getAllSoftDeleted() {
        List<ReviewDTO.Response> softDeletedReviews = reviewService.getAllSoftDeletedReviews();
        log.info("Fetched all soft-deleted reviews");
        return ResponseEntity.ok(softDeletedReviews);
    }

    @GetMapping("/customer/me")
    @Operation(summary = "[신랑신부] 내 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getMyReviewsForCustomer() {
        List<ReviewDTO.Response> myReviews = reviewService.getMyReviewsForCustomer();
        log.info("Fetched my reviews for customer");
        return ResponseEntity.ok(myReviews);
    }

    @GetMapping("/weddingplanner/me")
    @Operation(summary = "[웨딩플래너] 내 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getMyReviewsForWeddingplanner() {
        List<ReviewDTO.Response> myReviews = reviewService.getMyReviewsForWeddingplanner();
        log.info("Fetched my reviews for wedding planner");
        return ResponseEntity.ok(myReviews);
    }
}
