package com.example.demo.legacychat.repository;

import com.example.demo.legacychat.domain.Message;
import com.example.demo.portfolio.domain.Portfolio;
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

    @Query(value = "SELECT * from Message message WHERE room.is_deleted = true", nativeQuery = true)
    List<Portfolio> findSoftDeletedMessages();

}

