package com.example.demo.chat.service;

import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.domain.Message;
import com.example.demo.chat.domain.ReadFlag;
import com.example.demo.chat.dto.ChatRoomDTO;
import com.example.demo.chat.dto.ChatRoomOverviewDTO;
import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.chat.mapper.ChatRoomMapper;
import com.example.demo.chat.mapper.MessageMapper;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.chat.repository.MessageRepository;
import com.example.demo.chat.repository.ReadFlagRepository;
import com.example.demo.enums.member.MemberRole;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.service.CustomUserDetailsService;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioDTO;
import com.example.demo.portfolio.repository.PortfolioRepository;
import com.example.demo.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper = ChatRoomMapper.INSTANCE;

    private final CustomUserDetailsService customUserDetailsService;

    private final PortfolioService portfolioService;
    private final PortfolioRepository portfolioRepository;

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    private final ReadFlagRepository readFlagRepository;

    public ChatRoom getChatRoomById(Long chatRoomId) {
        log.info("Fetching chat room by ID: {}", chatRoomId);
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> {
                    log.error("Chat room not found with ID: {}", chatRoomId);
                    return new RuntimeException("ChatRoom not found");
                });
    }

    public ChatRoomDTO.Response enterChatRoomByPortfolioId(Long portfolioId) {
        log.info("Entering chat room by portfolio ID: {}", portfolioId);
        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer();
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> {
                    log.error("Portfolio not found with ID: {}", portfolioId);
                    return new RuntimeException("Portfolio not found");
                });
        WeddingPlanner weddingPlanner = portfolio.getWeddingPlanner();

        if (!isChatRoomExist(customer, weddingPlanner)) {
            log.info("Chat room does not exist for customer ID: {} and wedding planner ID: {}", customer.getId(), weddingPlanner.getId());
            return createChatRoomByPortfolioId(customer, weddingPlanner);
        }

        Long chatRoomId = getChatRoomIdByCustomerAndWeddingPlanner(customer, weddingPlanner);
        log.info("Chat room exists, entering existing chat room with ID: {}", chatRoomId);
        return getChatRoomByChatRoomId(chatRoomId);
    }

    public ChatRoomDTO.Response createChatRoomByPortfolioId(Customer customer, WeddingPlanner weddingPlanner) {
        log.info("Creating chat room for customer ID: {} and wedding planner ID: {}", customer.getId(), weddingPlanner.getId());
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
        log.info("Created chat room with ID: {}", chatRoom.getId());
        return chatRoomMapper.entityToResponse(chatRoom);
    }

    public List<ChatRoomOverviewDTO.Response> getCustomersAllChatRoom() {
        log.info("Fetching all chat rooms for customer");
        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer();
        List<ChatRoom> chatRooms = chatRoomRepository.findByCustomerId(customer.getId());

        return chatRooms.stream()
                .map(chatRoom -> {
                    Portfolio portfolio = portfolioService.getPortfolioByWeddingPlannerId(chatRoom.getWeddingPlanner().getId());
                    PortfolioDTO.Response portfolioResponse = portfolioService.getPortfolioById(portfolio.getId());
                    List<Message> messages = messageRepository.findByChatRoomId(chatRoom.getId());

                    return ChatRoomOverviewDTO.Response.builder()
                            .chatRoomId(chatRoom.getId())
                            .othersProfileImageUrl(portfolioResponse.getWeddingPlannerPortfolioResponse().getProfileImageUrl())
                            .othersName(portfolioResponse.getWeddingPlannerPortfolioResponse().getName())
                            .lastMessage(messages.get(messages.size() - 1).getContents())
                            .lastMessageCreatedAt(messages.get(messages.size() - 1).getCreatedAt())
                            .organizationName(portfolioResponse.getOrganization())
                            .portfolioId(portfolioResponse.getId())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<ChatRoomOverviewDTO.Response> getWeddingPlannersAllChatRoom() {
        log.info("Fetching all chat rooms for wedding planner");
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();
        List<ChatRoom> chatRooms = chatRoomRepository.findByWeddingPlannerId(weddingPlanner.getId());

        return chatRooms.stream()
                .map(chatRoom -> {
                    Customer customer = chatRoom.getCustomer();
                    List<Message> messages = messageRepository.findByChatRoomId(chatRoom.getId());

                    return ChatRoomOverviewDTO.Response.builder()
                            .chatRoomId(chatRoom.getId())
                            .othersProfileImageUrl(customer.getProfileImageUrl())
                            .othersName(customer.getName())
                            .lastMessage(messages.get(messages.size() - 1).getContents())
                            .lastMessageCreatedAt(messages.get(messages.size() - 1).getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public Long getChatRoomIdByCustomerAndWeddingPlanner(Customer customer, WeddingPlanner weddingPlanner) {
        log.info("Getting chat room ID for customer ID: {} and wedding planner ID: {}", customer.getId(), weddingPlanner.getId());
        ChatRoom chatRoom = chatRoomRepository.findByCustomerIdAndWeddingPlannerId(customer.getId(), weddingPlanner.getId());
        return chatRoom.getId();
    }

    public ChatRoomDTO.Response getChatRoomByChatRoomId(Long chatRoomId) {
        log.info("Fetching chat room by chat room ID: {}", chatRoomId);
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> {
                    log.error("Chat room not found with ID: {}", chatRoomId);
                    return new RuntimeException("ChatRoom not found");
                });

        ReadFlag customerReadFlag = readFlagRepository.findByChatRoomIdAndMemberRole(chatRoom.getId(), MemberRole.CUSTOMER)
                .orElseThrow(() -> {
                    log.error("ReadFlag not found for chat room ID: {} and role: CUSTOMER", chatRoomId);
                    return new RuntimeException("ReadFlag not found");
                });
        ReadFlag weddingPlannerReadFlag = readFlagRepository.findByChatRoomIdAndMemberRole(chatRoom.getId(), MemberRole.WEDDING_PLANNER)
                .orElseThrow(() -> {
                    log.error("ReadFlag not found for chat room ID: {} and role: WEDDING_PLANNER", chatRoomId);
                    return new RuntimeException("ReadFlag not found");
                });

        List<Message> messages = messageRepository.findByChatRoomId(chatRoom.getId());

        List<MessageDTO.Response> messageResponses = messages.stream()
                .map(messageMapper::entityToResponse)
                // set clubId to each Response
                .map(messageResponse -> {
                    messageResponse.setChatRoomId(chatRoomId);
                    return messageResponse;
                })
                .collect(Collectors.toList());

        ChatRoomDTO.Response response = chatRoomMapper.entityToResponse(chatRoom);

        response.setMessages(messageResponses);
        response.setCustomerLastReadMessageId(customerReadFlag.getLastReadMessageId());
        response.setWeddingPlannerLastReadMessageId(weddingPlannerReadFlag.getLastReadMessageId());

        return response;
    }

    public void deleteChatRoom(Long chatRoomId) {
        log.info("Deleting chat room with ID: {}", chatRoomId);
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> {
                    log.error("Chat room not found with ID: {}", chatRoomId);
                    return new RuntimeException("ChatRoom not found");
                });

        chatRoom.setDeleted(true);
        chatRoomRepository.save(chatRoom);
    }

    public boolean isChatRoomExist(Customer customer, WeddingPlanner weddingPlanner) {
        log.info("Checking if chat room exists for customer ID: {} and wedding planner ID: {}", customer.getId(), weddingPlanner.getId());
        return chatRoomRepository.existsByCustomerIdAndWeddingPlannerId(customer.getId(), weddingPlanner.getId());
    }
}
