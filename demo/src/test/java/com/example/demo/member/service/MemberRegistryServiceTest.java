package com.example.demo.member.service;

import com.example.demo.config.S3Uploader;
import com.example.demo.jwt.TokenProvider;
import com.example.demo.member.repository.CustomerRepository;
import com.example.demo.member.repository.WeddingPlannerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MemberRegistryServiceTest {

    @MockBean
    private S3Uploader s3Uploader;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private WeddingPlannerRepository weddingPlannerRepository;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private MemberRegistryService memberRegistryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


}
