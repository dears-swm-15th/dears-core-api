package com.teamdears.core.member.repository;

import com.teamdears.core.member.domain.WeddingPlanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeddingPlannerRepository extends JpaRepository<WeddingPlanner, Long> {

    Optional<WeddingPlanner> findByUUID(String separator);

    Optional<WeddingPlanner> findByPortfolioId(Long portfolioId);

    boolean existsByNickname(String nickname);
}
