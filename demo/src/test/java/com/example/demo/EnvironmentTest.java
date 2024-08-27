//package com.example.demo;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//public class EnvironmentTest {
//
//    @Autowired
//    private Environment environment;
//
//    @Test
//    public void testActiveProfiles() {
//        String[] activeProfiles = environment.getActiveProfiles();
//        assertEquals(1, activeProfiles.length);
//        assertEquals("test", activeProfiles[0]);
//    }
//
//    @Test
//    public void testDatabaseUrl() {
//        String url = environment.getProperty("spring.datasource.url");
//        assertTrue(url.contains("jdbc:h2:mem:testdb"));
//    }
//}
