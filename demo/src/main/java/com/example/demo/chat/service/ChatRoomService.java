//package com.example.demo.chat.service;
//
//
//import com.example.demo.chat.domain.ChatRoom;
//import com.example.demo.chat.domain.ReadFlag;
//import com.example.demo.chat.dto.ChatRoomDTO;
//import com.example.demo.chat.mapper.ChatRoomMapper;
//import com.example.demo.chat.repository.ChatRoomRepository;
//import com.example.demo.enums.member.MemberRole;
//import com.example.demo.member.domain.Customer;
//import com.example.demo.member.domain.WeddingPlanner;
//import com.example.demo.member.service.CustomUserDetailsService;
//import com.example.demo.portfolio.dto.PortfolioDTO;
//import com.example.demo.portfolio.service.PortfolioService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ChatRoomService {
//    private final ChatRoomRepository chatRoomRepository;
//    private final ChatRoomMapper chatRoomMapper = ChatRoomMapper.INSTANCE;
//
//    private final CustomUserDetailsService customUserDetailsService;
//    private final PortfolioService portfolioService;
//
//    public ChatRoomDTO.Response createChatRoomByPortfolioId(Long portfolioId) {
//
//        ChatRoom chatRoom = ChatRoom.builder().build();
//
//        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer().get();
//        PortfolioDTO.Response portfolioResponse = portfolioService.getPortfolioById(portfolioId);
//
//        WeddingPlanner weddingPlanner = portfolioResponse.getWeddingPlanner();
//
//        chatRoom.setCustomer(customer);
//        chatRoom.setWeddingPlanner(weddingPlanner);
//
//        ReadFlag customerReadFlag = ReadFlag.builder()
//                .chatRoom(chatRoom)
//                .build();
//
//        ReadFlag weddingPlannerReadFlag = ReadFlag.builder()
//                .chatRoom(chatRoom)
//                .build();
//
//        chatRoom.addReadFlag(MemberRole.CUSTOMER, customerReadFlag);
//        chatRoom.addReadFlag(MemberRole.WEDDING_PLANNER, weddingPlannerReadFlag);
//
//        chatRoomRepository.save(chatRoom);
//
//        return chatRoomMapper.entityToResponse(chatRoom);
//
//    }
//
//    public List<ChatRoomDTO.Response> getCurrentUsersAllChatRoom() {
//        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer().get();
//
//        List<ChatRoom> chatRooms = chatRoomRepository.findByCustomerId(customer.getId());
//
//        return chatRoomRepository.findByCustomerId(customer.getId()).stream()
//                .map(chatRoomMapper::entityToResponse)
//                .collect(Collectors.toList());
//    }
//
//    public ChatRoomDTO.Response getChatRoomById(Long chatRoomId) {
//        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
//                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
//
//        return chatRoomMapper.entityToResponse(chatRoom);
//    }
//
//    public void deleteChatRoom(Long chatRoomId) {
//        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
//                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
//
//        chatRoom.setDeleted(true);
//        chatRoomRepository.save(chatRoom);
//    }
//
//
//
//}
//
