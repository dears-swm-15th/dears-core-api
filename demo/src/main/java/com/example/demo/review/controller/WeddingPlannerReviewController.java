package com.example.demo.review.controller;

import com.example.demo.review.dto.ReviewDTO;
import com.example.demo.review.service.WeddingPlannerReviewServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weddingplanner/review")
@Tag(name = "review", description = "웨딩플래너 리뷰 API")
public class WeddingPlannerReviewController {
    private final WeddingPlannerReviewServiceImpl weddingPlannerReviewService;

    @PostMapping("/create")
    @Operation(summary = "[웨딩플래너] 리뷰 작성")
    public ResponseEntity<ReviewDTO.Response> createReviewForWeddingPlanner(@RequestBody ReviewDTO.Request reviewRequest) {
        ReviewDTO.Response createdReview = weddingPlannerReviewService.createReview(reviewRequest);
        return ResponseEntity.ok(createdReview);
    }

    @PostMapping("/update/{id}")
    @Operation(summary = "[웨딩플래너] 특정 리뷰 업데이트")
    public ResponseEntity<ReviewDTO.Response> updateReviewForWeddingPlanner(
            @Parameter(description = "reviewId")
            @PathVariable Long id, @RequestBody ReviewDTO.Request reviewRequest) {
        ReviewDTO.Response updatedReview = weddingPlannerReviewService.modifyReview(id, reviewRequest);
        return ResponseEntity.ok(updatedReview);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "[웨딩플래너] 특정 리뷰 삭제")
    public ResponseEntity<Void> deleteReviewForWeddingPlanner(
            @Parameter(description = "reviewId")
            @PathVariable Long id) {
        weddingPlannerReviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get")
    @Operation(summary = "[웨딩플래너] 내 리뷰 조회")
    public ResponseEntity<List<ReviewDTO.Response>> getMyReviewsForWeddingplanner() {

        List<ReviewDTO.Response> myReviews = weddingPlannerReviewService.getMyReviews();
        return ResponseEntity.ok(myReviews);
    }

}
