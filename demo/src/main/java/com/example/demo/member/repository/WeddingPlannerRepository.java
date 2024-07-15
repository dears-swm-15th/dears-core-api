package com.example.demo.member.repository;

import com.example.demo.member.domain.WeddingPlanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeddingPlannerRepository extends JpaRepository<WeddingPlanner, Long> {

    Optional<WeddingPlanner> findByUUID(String separator);

}
