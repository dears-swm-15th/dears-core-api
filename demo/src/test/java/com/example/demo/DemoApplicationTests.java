package com.example.demo;

import com.example.demo.config.S3Uploader;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DemoApplicationTests {

    @MockBean
    private S3Uploader s3Uploader;

    @Test
    void contextLoads() {
    }

}
