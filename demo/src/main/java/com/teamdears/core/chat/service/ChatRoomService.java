package com.teamdears.core.chat.service;

import com.teamdears.core.chat.domain.ChatRoom;
import com.teamdears.core.chat.domain.Message;
import com.teamdears.core.chat.dto.ChatRoomDTO;
import com.teamdears.core.chat.dto.ChatRoomOverviewDTO;
import com.teamdears.core.chat.dto.MessageDTO;
import com.teamdears.core.chat.mapper.ChatRoomMapper;
import com.teamdears.core.chat.mapper.MessageMapper;
import com.teamdears.core.chat.repository.ChatRoomRepository;
import com.teamdears.core.config.StompPreHandler;
import com.teamdears.core.enums.chat.MessageType;
import com.teamdears.core.enums.member.MemberRole;
import com.teamdears.core.member.domain.Customer;
import com.teamdears.core.member.domain.WeddingPlanner;
import com.teamdears.core.member.repository.WeddingPlannerRepository;
import com.teamdears.core.member.service.CustomUserDetailsService;
import com.teamdears.core.portfolio.domain.Portfolio;
import com.teamdears.core.portfolio.dto.PortfolioDTO;
import com.teamdears.core.portfolio.repository.PortfolioRepository;
import com.teamdears.core.portfolio.service.PortfolioService;
import com.teamdears.core.redis.service.RedisService;
import com.teamdears.core.wishlist.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
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

    private final WishListService wishListService;

    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    private final WeddingPlannerRepository weddingPlannerRepository;

    private final SimpMessagingTemplate template;
    private final RedisService redisService;

    Duration DAY_TIMEOUT = Duration.ofDays(1);

    public ChatRoom getChatRoomById(Long chatRoomId) {
        log.info("Fetching chat room by ID: {}", chatRoomId);
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> {
                    log.error("Chat room not found with ID: {}", chatRoomId);
                    return new RuntimeException("ChatRoom not found");
                });
    }

    public void sendNewChatRoomTrigger(Long portfolioId, Long chatRoomId) {
        WeddingPlanner weddingPlanner = weddingPlannerRepository.findByPortfolioId(portfolioId)
                .orElseThrow(() -> {
                    log.error("Wedding planner not found with portfolio ID: {}", portfolioId);
                    return new RuntimeException("WeddingPlanner not found");
                });

        String weddingPlannerUuid = weddingPlanner.getUUID();
        boolean isOppositeConnected = StompPreHandler.isUserConnected(weddingPlannerUuid);

        log.info("Wedding Planner UUID: {}", weddingPlannerUuid);
        log.info("Connected: {}", isOppositeConnected);

        if (isOppositeConnected) {
            MessageDTO.Request messageRequest = MessageDTO.Request.builder()
                    .chatRoomId(chatRoomId)
                    .messageType(MessageType.ENTER)
                    .senderRole(MemberRole.CUSTOMER)
                    .content("New Chat Room Created")
                    .build();

            template.convertAndSend("/sub/" + weddingPlannerUuid, messageRequest);
        }
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
            ChatRoomDTO.Response createdChatRoomResponse = createChatRoomByPortfolioId(customer, weddingPlanner);

            sendNewChatRoomTrigger(portfolioId, createdChatRoomResponse.getChatRoomId());
        }

        ChatRoom chatRoom = chatRoomRepository.findByCustomerIdAndWeddingPlannerId(customer.getId(), weddingPlanner.getId());

        return getMessagesByChatRoomForCustomer(chatRoom);
    }

    public ChatRoomDTO.Response createChatRoomByPortfolioId(Customer customer, WeddingPlanner weddingPlanner) {
        log.info("Creating chat room for customer ID: {} and wedding planner ID: {}", customer.getId(), weddingPlanner.getId());
        ChatRoom chatRoom = ChatRoom.builder().build();

        chatRoom.setCustomer(customer);
        chatRoom.setWeddingPlanner(weddingPlanner);
        chatRoom.setMessages(List.of());

        chatRoomRepository.save(chatRoom);
        log.info("Created chat room with ID: {}", chatRoom.getId());

        return ChatRoomDTO.Response.builder()
                .messages(List.of())
                .userIds(new HashSet<>())
                .chatRoomId(chatRoom.getId())
                .build();
    }

    public Integer getCustomersUnreadCount(String customerUuid) {
        log.info("Fetching all chat rooms's unreadMessages for customer");
        Customer customer = customUserDetailsService.getCustomerByUuid(customerUuid);
        List<ChatRoom> chatRooms = chatRoomRepository.findByCustomerId(customer.getId());

        return chatRooms.stream()
                .map(chatRoom ->
                        chatRoomRepository.countUnreadMessages(chatRoom.getId(), MemberRole.WEDDING_PLANNER)
                )
                .reduce(0, Integer::sum);
    }

    public Integer getWeddingPlannersUnreadCount(String weddingPlannerUuid) {
        log.info("Fetching all chat rooms's unreadMessages for wedding planner");
        WeddingPlanner weddingPlanner = customUserDetailsService.getWeddingPlannerByUuid(weddingPlannerUuid);
        List<ChatRoom> chatRooms = chatRoomRepository.findByWeddingPlannerId(weddingPlanner.getId());

        return chatRooms.stream()
                .map(chatRoom ->
                        chatRoomRepository.countUnreadMessages(chatRoom.getId(), MemberRole.CUSTOMER)
                )
                .reduce(0, Integer::sum);
    }

    public List<ChatRoomOverviewDTO.Response> getCustomersAllChatRoom() {
        log.info("Fetching all chat rooms for customer");
        Customer customer = customUserDetailsService.getCurrentAuthenticatedCustomer();
        List<ChatRoom> chatRooms = chatRoomRepository.findByCustomerIdOrderByLastMessageCreatedAtDesc(customer.getId());

        return chatRooms.stream()
                .map(chatRoom -> {
                    Portfolio portfolio = portfolioService.getPortfolioByWeddingPlannerId(chatRoom.getWeddingPlanner().getId());
                    PortfolioDTO.Response portfolioResponse = portfolioService.getPortfolioById(portfolio.getId());

                    return ChatRoomOverviewDTO.Response.builder()
                            .chatRoomId(chatRoom.getId())
                            .othersProfileImageUrl(portfolioResponse.getProfileImageUrl())
                            .othersName(portfolioResponse.getPlannerName())
                            .lastMessage(chatRoom.getLastMessageContent())
                            .lastMessageCreatedAt(chatRoom.getLastMessageCreatedAt())
                            .organizationName(portfolioResponse.getOrganization())
                            .portfolioId(portfolioResponse.getId())
                            .unreadMessageCount(getCustomersUnreadCount(customer.getUUID()))
                            .isWishListed(wishListService.isWishListed(portfolioResponse.getId()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<ChatRoomOverviewDTO.Response> getWeddingPlannersAllChatRoom() {
        log.info("Fetching all chat rooms for wedding planner");
        WeddingPlanner weddingPlanner = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner();
        List<ChatRoom> chatRooms = chatRoomRepository.findByWeddingPlannerIdOrderByLastMessageCreatedAtDesc(weddingPlanner.getId());

        return chatRooms.stream()
                .map(chatRoom -> {
                    Customer customer = chatRoom.getCustomer();

                    return ChatRoomOverviewDTO.Response.builder()
                            .chatRoomId(chatRoom.getId())
                            .othersProfileImageUrl(customer.getProfileImageUrl())
                            .othersName(customer.getName())
                            .lastMessage(chatRoom.getLastMessageContent())
                            .lastMessageCreatedAt(chatRoom.getLastMessageCreatedAt())
                            .unreadMessageCount(getWeddingPlannersUnreadCount(weddingPlanner.getUUID()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    public ChatRoomDTO.Response getMessagesByChatRoomIdForCustomer(Long chatRoomId) {
        log.info("Fetching messages by chat room ID: {}", chatRoomId);
        ChatRoom chatRoom = getChatRoomById(chatRoomId);

        updateOppositeReadFlag(chatRoom);
        return getMessagesByChatRoomForCustomer(chatRoom);
    }

    public ChatRoomDTO.Response getMessagesByChatRoomIdForWeddingPlanner(Long chatRoomId) {
        log.info("Fetching messages by chat room ID: {}", chatRoomId);
        ChatRoom chatRoom = getChatRoomById(chatRoomId);

        updateOppositeReadFlag(chatRoom);
        return getMessagesByChatRoomForWeddingPlanner(chatRoom);
    }

    public ChatRoomDTO.Response getMessagesByChatRoomForCustomer(ChatRoom chatRoom) {
        log.info("Fetching messages by chat room for customer");
        String Uuid = customUserDetailsService.getCurrentAuthenticatedCustomer().getUUID();
        redisService.setSetValue(chatRoom.getId().toString(), Uuid, DAY_TIMEOUT);

        chatRoomRepository.save(chatRoom);

        List<Message> messages = chatRoom.getMessages();
        List<MessageDTO.Response> messageResponses = messages.stream()
                .map(messageMapper::entityToResponse)// set clubId to each Response
                .collect(Collectors.toList());

        ChatRoomDTO.Response response = chatRoomMapper.entityToResponse(chatRoom);
        response.setMessages(messageResponses);
        response.setChatRoomId(chatRoom.getId());

        return response;
    }

    public ChatRoomDTO.Response getMessagesByChatRoomForWeddingPlanner(ChatRoom chatRoom) {
        log.info("Fetching messages by chat room for wedding planner");
        String Uuid = customUserDetailsService.getCurrentAuthenticatedWeddingPlanner().getUUID();
        redisService.setSetValue(chatRoom.getId().toString(), Uuid, DAY_TIMEOUT);

        chatRoomRepository.save(chatRoom);

        List<Message> messages = chatRoom.getMessages();
        List<MessageDTO.Response> messageResponses = messages.stream()
                .map(messageMapper::entityToResponse)// set clubId to each Response
                .collect(Collectors.toList());

        ChatRoomDTO.Response response = chatRoomMapper.entityToResponse(chatRoom);
        response.setMessages(messageResponses);

        return response;
    }

    private void updateOppositeReadFlag(ChatRoom chatRoom) {
        log.info("Updating opposite read flag for chat room with ID: {}", chatRoom.getId());
        List<Message> messages = chatRoom.getMessages();

        MemberRole memberRole = customUserDetailsService.getCurrentAuthenticatedMemberRole();
        for (Message message : messages) {
            if (message.getSenderRole() != memberRole) {
                message.setOppositeReadFlag(true);
            }
        }
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
