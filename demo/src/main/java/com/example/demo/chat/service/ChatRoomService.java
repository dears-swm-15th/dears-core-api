package com.example.demo.chat.service;


import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.domain.Message;
import com.example.demo.chat.domain.ReadFlag;
import com.example.demo.chat.dto.ChatRoomDTO;
import com.example.demo.chat.dto.ChatRoomOverviewDTO;
import com.example.demo.chat.dto.ReadFlagDTO;
import com.example.demo.chat.mapper.ChatRoomMapper;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.chat.repository.MessageRepository;
import com.example.demo.chat.repository.ReadFlagRepository;
import com.example.demo.enums.member.MemberRole;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioDTO;
import com.example.demo.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper = ChatRoomMapper.INSTANCE;

    private final CustomUserDetailsService customUserDetailsService;
    private final PortfolioService portfolioService;

    private final ReadFlagRepository readFlagRepository;
    private final MessageRepository messageRepository;

    public ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
    }

    public ChatRoomDTO.Response enterChatRoomByPortfolioId(Long portfolioId) {
        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer();
        PortfolioDTO.Response portfolioResponse = portfolioService.getPortfolioById(portfolioId);

        WeddingPlanner weddingPlanner = portfolioResponse.getWeddingPlanner();

        if (!isChatRoomExist(customer, weddingPlanner)) {
            System.out.println("ChatRoom not exist");
            return createChatRoomByPortfolioId(customer, weddingPlanner);
        }
        System.out.println("ChatRoom exist");
        Long chatRoomId = getChatRoomIdByCustomerAndWeddingPlanner(customer, weddingPlanner);
        return getChatRoomByChatRoomId(chatRoomId);
    }

    public ChatRoomDTO.Response createChatRoomByPortfolioId(Customer customer, WeddingPlanner weddingPlanner) {

        ChatRoom chatRoom = ChatRoom.builder().build();

        chatRoom.setCustomer(customer);
        chatRoom.setWeddingPlanner(weddingPlanner);

        ReadFlag customerReadFlag = ReadFlag.builder()
                .memberRole(MemberRole.CUSTOMER)
                .lastReadMessageId(0L)
                .chatRoom(chatRoom)
                .build();

        ReadFlag weddingPlannerReadFlag = ReadFlag.builder()
                .memberRole(MemberRole.WEDDING_PLANNER)
                .lastReadMessageId(0L)
                .chatRoom(chatRoom)
                .build();

        chatRoomRepository.save(chatRoom);
        readFlagRepository.save(customerReadFlag);
        readFlagRepository.save(weddingPlannerReadFlag);

        return chatRoomMapper.entityToResponse(chatRoom);
    }

    public List<ChatRoomOverviewDTO.Response> getCurrentUsersAllChatRoom() {
        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer();
        List<ChatRoom> chatRooms = chatRoomRepository.findByCustomerId(customer.getId());

        return chatRooms.stream()
                .map(chatRoom -> {
                    Portfolio portfolio = portfolioService.getPortfolioByWeddingPlannerId(chatRoom.getWeddingPlanner().getId());
                    PortfolioDTO.Response portfolioResponse = portfolioService.getPortfolioById(portfolio.getId());

                    List<Message> messages = messageRepository.findByChatRoomId(chatRoom.getId())
                            .orElse(List.of());

                    return ChatRoomOverviewDTO.Response.builder()
                            .weddingPlannerProfileImageUrl(portfolioResponse.getWeddingPlanner().getProfileImageUrl())
                            .weddingPlannerName(portfolioResponse.getWeddingPlanner().getName())
                            .lastMessage(messages.get(messages.size() - 1).getContents())
                            .lastMessageCreatedAt(messages.get(messages.size() - 1).getCreatedAt())
                            .organizationName(portfolioResponse.getOrganization())
                            .portfolioId(portfolioResponse.getWeddingPlanner().getId())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public Long getChatRoomIdByCustomerAndWeddingPlanner(Customer customer, WeddingPlanner weddingPlanner) {
        ChatRoom chatRoom = chatRoomRepository.findByCustomerIdAndWeddingPlannerId(customer.getId(), weddingPlanner.getId());
        return chatRoom.getId();
    }

    public ChatRoomDTO.Response getChatRoomByChatRoomId(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        ReadFlag customerReadFlag = readFlagRepository.findByChatRoomIdAndMemberRole(chatRoom.getId(), MemberRole.CUSTOMER)
                .orElseThrow(() -> new RuntimeException("ReadFlag not found"));
        ReadFlag weddingPlannerReadFlag = readFlagRepository.findByChatRoomIdAndMemberRole(chatRoom.getId(), MemberRole.WEDDING_PLANNER)
                .orElseThrow(() -> new RuntimeException("ReadFlag not found"));

        List<Message> messages = messageRepository.findByChatRoomId(chatRoom.getId())
                .orElse(List.of());

        ChatRoomDTO.Response response = chatRoomMapper.entityToResponse(chatRoom);
        response.setMessages(messages);
        response.setCustomerLastReadMessageId(customerReadFlag.getLastReadMessageId());
        response.setWeddingPlannerLastReadMessageId(weddingPlannerReadFlag.getLastReadMessageId());

        return response;
    }


    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        chatRoom.setDeleted(true);
        chatRoomRepository.save(chatRoom);
    }


    public boolean isChatRoomExist(Customer customer, WeddingPlanner weddingPlanner) {
        return chatRoomRepository.existsByCustomerIdAndWeddingPlannerId(customer.getId(), weddingPlanner.getId());
    }
}

