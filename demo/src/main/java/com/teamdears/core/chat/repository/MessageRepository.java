package com.teamdears.core.chat.repository;

import com.teamdears.core.chat.domain.Message;
import com.teamdears.core.portfolio.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.isDeleted = true WHERE m.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query(value = "SELECT * from Message m WHERE m.is_deleted = true", nativeQuery = true)
    List<Portfolio> findSoftDeletedMessages();

}
