package com.example.demo.chat.repository;

import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.domain.Message;
import com.example.demo.config.S3Uploader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.demo.enums.member.MemberRole.CUSTOMER;
import static com.example.demo.enums.member.MemberRole.WEDDING_PLANNER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ChatRoomRepositoryTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @MockBean
    private S3Uploader s3Uploader;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    @DisplayName("읽지 않은 상대방의 메세지 개수를 조회한다")
    void countUnreadMessages() {
        // given

        ChatRoom chatRoom = ChatRoom.builder()
                .build();

        Message message1 = Message.builder()
                .chatRoom(chatRoom)
                .senderRole(CUSTOMER)
                .oppositeReadFlag(false)
                .build();

        Message message2 = Message.builder()
                .chatRoom(chatRoom)
                .senderRole(CUSTOMER)
                .oppositeReadFlag(true)
                .build();

        Message message3 = Message.builder()
                .chatRoom(chatRoom)
                .senderRole(WEDDING_PLANNER)
                .oppositeReadFlag(false)
                .build();

        Message message4 = Message.builder()
                .chatRoom(chatRoom)
                .senderRole(WEDDING_PLANNER)
                .oppositeReadFlag(true)
                .build();

        chatRoomRepository.save(chatRoom);
        messageRepository.saveAll(List.of(message1, message2, message3, message4));

        // when
        int unreadMessageCount = chatRoomRepository.countUnreadMessages(chatRoom.getId(), CUSTOMER);

        // then
        assertThat(unreadMessageCount).isEqualTo(1);
    }


}