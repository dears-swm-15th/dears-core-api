//package com.example.demo.chat.service;
//
//import com.example.demo.chat.domain.ChatRoom;
//import com.example.demo.chat.domain.Message;
//import com.example.demo.chat.dto.ChatRoomDTO;
//import com.example.demo.chat.dto.MessageDTO;
//import com.example.demo.chat.repository.ChatRoomRepository;
//import com.example.demo.chat.repository.MessageRepository;
//import com.example.demo.member.domain.Member;
//import com.example.demo.member.repository.MemberRepository;
//import com.example.demo.portfolio.domain.Portfolio;
//import com.example.demo.portfolio.repository.PortfolioRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ChatService {
//
//    private final ChatRoomRepository chatRoomRepository;
//    private final MessageRepository messageRepository;
//    private final MemberRepository memberRepository;
//    private final PortfolioRepository portfolioRepository;
//
//
//    public void startChat(Long coustomerId, Long portfolioId) {
//
//    }
//
//    // validation
//    private void checkWeddingPlannerStartsChat() {}
//
//    private boolean isFirstChatting() {}
//
//    private MessageDTO.Response saveMessage() {}
//
//    private ChatRoomDTO.Response getChatRoomById() {}
//
//    private List<MessageDTO.Response> getMessages(Long chatRoomId) {}
//
//    private void reduceMessageReadCount() {}
//
//    private void checkMessageReadCount(Message message) {}
//    // count 관련 edge 처리
//    // sender, receiver 관련 validation
//
//    // TODO
//    // makeReservation
//
//}
