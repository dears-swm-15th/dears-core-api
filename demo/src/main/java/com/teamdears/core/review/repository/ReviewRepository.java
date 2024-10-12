package com.teamdears.core.review.repository;

import com.teamdears.core.portfolio.domain.Portfolio;
import com.teamdears.core.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByPortfolio(Portfolio portfolio);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Review r SET r.isDeleted = true WHERE r.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query("SELECT r from Review r WHERE r.isDeleted = true")
    List<Review> findSoftDeletedReviews();

    @Query("SELECT r from Review r WHERE r.isProvided = true and r.reviewerId = :weddingPlannerId")
    List<Review> findReviewsForWeddingPlanner(Long weddingPlannerId);

    @Query("SELECT r from Review r WHERE r.isProvided = false and r.reviewerId = :customerId")
    List<Review> findReviewsForCustomer(Long customerId);

    Integer countByPortfolioId(Long id);
}