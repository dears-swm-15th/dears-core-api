package com.teamdears.core.chat.repository;

import com.teamdears.core.chat.domain.ChatRoom;
import com.teamdears.core.enums.member.MemberRole;
import com.teamdears.core.portfolio.domain.Portfolio;
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

    List<ChatRoom> findByWeddingPlannerId(Long weddingPlannerId);

    ChatRoom findByCustomerIdAndWeddingPlannerId(Long customerId, Long weddingPlannerId);

    List<ChatRoom> findByCustomerIdOrderByLastMessageCreatedAtDesc(Long customerId);

    List<ChatRoom> findByWeddingPlannerIdOrderByLastMessageCreatedAtDesc(Long weddingPlannerId);

    @Transactional
    @Modifying
    @Query("UPDATE ChatRoom c SET c.isDeleted = true WHERE c.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query(value = "SELECT * from ChatRoom chat_room WHERE chat_room.is_deleted = true", nativeQuery = true)
    List<Portfolio> findSoftDeletedChatRooms();

    @Query("SELECT COUNT(m) FROM Message m WHERE m.chatRoom.id = :chatRoomId AND m.senderRole = :senderRole AND m.oppositeReadFlag = false")
    int countUnreadMessages(@Param("chatRoomId") Long chatRoomId, @Param("senderRole") MemberRole senderRole);

}
