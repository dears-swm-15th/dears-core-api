package com.example.demo.chat.service;

import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.chat.repository.MessageRepository;
import com.example.demo.config.S3Uploader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ChatRoomServiceTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @MockBean
    private S3Uploader s3Uploader;

    @Autowired
    private MessageRepository messageRepository;


    /*
    TODO : 메서드 컨벤션 통일
    TODO : 채팅방 ID로 조회하는 경우와 채팅방 자체로 조회하는 경우

     */


    @Test
    @DisplayName("새로운 채팅방이 생성되면 웨딩플래너에게 알림을 보낸다")
    void notifyWeddingPlannerOfNewChatRoom() {
        // TODO : 한 메서드 내에 책임이 너무 많지 않은가

        // given

        // when

        // then
    }

    @Test
    @DisplayName("포트폴리오 ID로 채팅방에 입장한다")
    void enterChatRoomByPortfolioId() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("포트폴리오 ID로 채팅방을 생성한다")
    void createChatRoomByPortfolioId() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("고객 : UUID로 읽지 않은 메세지 개수를 조회한다")
    void countUnreadMessagesByCustomerUuid() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("웨딩플래너 : UUID로 읽지 않은 메세지 개수를 조회한다")
    void countUnreadMessagesByWeddingPlannerUuid() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("고객 : 현재 유저의 모든 채팅방을 조회한다")
    void getCustomerALlChatRoom() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("웨딩플래너 : 현재 유저의 모든 채팅방을 조회한다")
    void getWeddingPlannerAllChatRoom() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("고객 : 채팅방 ID로 메세지를 조회한다")
    void getMessagesByChatRoomIdForCustomer() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("웨딩플래너 : 채팅방 ID로 메세지를 조회한다")
    void getMessagesByChatRoomIdForWeddingPlanner() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("고객 : 채팅방으로 메세지를 조회한다")
    void getMessagesByChatRoomForCustomer() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("웨딩플래너 : 채팅방으로 메세지를 조회한다")
    void getMessagesByChatRoomForWeddingPlanner() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("상대방이 보낸 메세지를 읽음 처리한다")
    void updateOppositeReadFlag() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("채팅방 ID로 채팅방을 삭제한다")
    void deleteChatRoom() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("고객과 웨딩플래너로 채팅방이 존재하는지 확인한다")
    void isChatRoomExist() {
        // TODO : Customer와 WeddingPlanner 직접 주입 받는 방식 과연 좋은가

        // given

        // when

        // then
    }

}