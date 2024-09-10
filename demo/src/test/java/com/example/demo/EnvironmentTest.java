package com.example.demo;

import com.example.demo.config.S3Uploader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class EnvironmentTest {

    @Autowired
    private Environment environment;

    @MockBean
    private S3Uploader s3Uploader;

    @Test
    @DisplayName("프로파일이 올바르게 설정되어 있는지 확인")
    public void testActiveProfiles() {
        String[] activeProfiles = environment.getActiveProfiles();
        assertEquals(1, activeProfiles.length);
        assertEquals("test", activeProfiles[0]);
    }
}
