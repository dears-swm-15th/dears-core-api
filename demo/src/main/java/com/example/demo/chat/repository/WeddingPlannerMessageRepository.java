package com.example.demo.chat.repository;

import com.example.demo.chat.domain.WeddingPlannerMessage;
import com.example.demo.portfolio.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WeddingPlannerMessageRepository extends JpaRepository<WeddingPlannerMessage, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE WeddingPlannerMessage w SET w.isDeleted = true WHERE w.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query(value = "SELECT * from WeddingPlannerMessage w WHERE w.is_deleted = true", nativeQuery = true)
    List<Portfolio> findSoftDeletedChatRooms();

}
