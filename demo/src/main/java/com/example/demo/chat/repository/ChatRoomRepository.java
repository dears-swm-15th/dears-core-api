package com.example.demo.chat.repository;

import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.portfolio.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    boolean existsByCustomerIdAndWeddingPlannerId(Long customerId, Long weddingPlannerId);

    List<ChatRoom> findByCustomerId(Long customerId);
    ChatRoom findByCustomerIdAndWeddingPlannerId(Long customerId, Long weddingPlannerId);

    @Transactional
    @Modifying
    @Query("UPDATE ChatRoom c SET c.isDeleted = true WHERE c.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query(value = "SELECT * from ChatRoom chat_room WHERE chat_room.is_deleted = true", nativeQuery = true)
    List<Portfolio> findSoftDeletedChatRooms();

}
