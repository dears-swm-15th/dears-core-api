package com.example.demo.review.repository;

import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Review r SET r.isDeleted = true WHERE r.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query(value = "SELECT * from Review review WHERE review.is_deleted = true", nativeQuery = true)
    List<Review> findSoftDeletedReviews();

}