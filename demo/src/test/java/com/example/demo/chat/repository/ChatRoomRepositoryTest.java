package com.example.demo.chat.repository;

import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.config.S3Uploader;
import com.example.demo.member.domain.Customer;
import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
class ChatRoomRepositoryTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @MockBean
    private S3Uploader s3Uploader;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("고객 ID로 채팅방을 조회한다.")
    void findByCustomerId() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .build();

        ChatRoom chatRoom1 = ChatRoom.builder()
                .customer(customer)
                .build();

        ChatRoom chatRoom2 = ChatRoom.builder()
                .customer(customer)
                .build();

        customerRepository.save(customer);
        chatRoomRepository.saveAll(List.of(chatRoom1, chatRoom2));

        // when
        List<ChatRoom> chatRooms = chatRoomRepository.findByCustomerId(customer.getId());

        // then
        assertThat(chatRooms.size()).isEqualTo(2);
        assertThat(chatRooms.get(0).getCustomer().getId()).isEqualTo(customer.getId());
        assertThat(chatRooms.get(1).getCustomer().getId()).isEqualTo(customer.getId());
    }

    @Test
    @DisplayName("웨딩플래너 ID로 채팅방을 조회한다.")
    void findByWeddingPlannerId() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .build();

        ChatRoom chatRoom1 = ChatRoom.builder()
                .customer(customer)
                .build();

        ChatRoom chatRoom2 = ChatRoom.builder()
                .customer(customer)
                .build();

        customerRepository.save(customer);
        chatRoomRepository.saveAll(List.of(chatRoom1, chatRoom2));

        // when
        List<ChatRoom> chatRooms = chatRoomRepository.findByCustomerId(customer.getId());

        // then
        assertThat(chatRooms.size()).isEqualTo(2);
        assertThat(chatRooms.get(0).getCustomer().getId()).isEqualTo(customer.getId());
        assertThat(chatRooms.get(1).getCustomer().getId()).isEqualTo(customer.getId());
    }

    @Test
    @DisplayName("고객 ID와 웨딩플래너 ID로 채팅방을 조회한다.")
    void findByCustomerIdAndWeddingPlannerId() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .build();

        WeddingPlanner weddingPlanner = WeddingPlanner.builder()
                .id(1L)
                .build();

        ChatRoom chatRoom = ChatRoom.builder()
                .customer(customer)
                .weddingPlanner(weddingPlanner)
                .build();

        customerRepository.save(customer);
        chatRoomRepository.save(chatRoom);

        // when
        ChatRoom findChatRoom = chatRoomRepository.findByCustomerIdAndWeddingPlannerId(customer.getId(), weddingPlanner.getId());

        // then
        assertThat(findChatRoom.getCustomer().getId()).isEqualTo(customer.getId());
    }

    @Test
    @DisplayName("고객 ID로 마지막 메세지가 생성된 순서로 채팅방을 조회한다.")
    void findByCustomerIdOrderByLastMessageCreatedAtDesc() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .build();

        ChatRoom chatRoom1 = ChatRoom.builder()
                .customer(customer)
                .build();

        ChatRoom chatRoom2 = ChatRoom.builder()
                .customer(customer)
                .build();

        customerRepository.save(customer);
        chatRoomRepository.saveAll(List.of(chatRoom1, chatRoom2));

        // when
        List<ChatRoom> chatRooms = chatRoomRepository.findByCustomerIdOrderByLastMessageCreatedAtDesc(customer.getId());

        // then
        assertThat(chatRooms.size()).isEqualTo(2);
        assertThat(chatRooms.get(0).getCustomer().getId()).isEqualTo(customer.getId());
        assertThat(chatRooms.get(1).getCustomer().getId()).isEqualTo(customer.getId());
    }

    
}