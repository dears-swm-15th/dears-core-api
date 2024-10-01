package com.example.demo.portfolio.repository;

import com.example.demo.portfolio.domain.Portfolio;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Optional<Portfolio> findByWeddingPlannerId(Long weddingPlannerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Portfolio p where p.id = :id")
    Optional<Portfolio> findPortfolioByIdWithPessimisticLock(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Portfolio p SET p.isDeleted = true WHERE p.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query(value = "SELECT p from Portfolio p WHERE p.isDeleted = true")
    List<Portfolio> findSoftDeletedPortfolios();

    @Transactional
    @Modifying
    @Query("UPDATE Portfolio p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
    void increaseViewCount(@Param("id") Long id);

    List<Portfolio> findTop5ByOrderByViewCountDesc();

    @Query(value = "SELECT DISTINCT p.* FROM portfolio p JOIN portfolio_services ps ON p.portfolio_id = ps.portfolio_id " +
            "WHERE ps.service_value LIKE %:keyword% OR p.planner_name LIKE %:keyword% OR " +
            "p.organization LIKE %:keyword% OR p.introduction LIKE %:keyword%",
            nativeQuery = true)
    List<Portfolio> searchByKeyword(@Param("keyword") String keyword);

}
