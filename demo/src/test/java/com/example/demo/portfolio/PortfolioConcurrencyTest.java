package com.example.demo.portfolio;

import com.example.demo.config.S3Config;
import com.example.demo.config.S3Uploader;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.repository.PortfolioRepository;
import com.example.demo.portfolio.service.PortfolioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.testng.Assert.assertEquals;

@SpringBootTest
public class PortfolioConcurrencyTest {


    @MockBean
    private S3Uploader s3Uploader;

    @MockBean
    private S3Config s3Config;


    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioService portfolioService;

    private Portfolio portfolio;

    @BeforeEach
    public void before() {
        portfolio = portfolioRepository.findById(1L).orElseThrow();
    }

    @Test
    public void 텀을_두고_100개_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            Thread.sleep(100);
            executorService.submit(() -> {
                try {
                    portfolioService.addWishListCount(portfolio.getId());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        Portfolio updatedPortfolio = portfolioRepository.findById(portfolio.getId()).orElseThrow();
        assertEquals(updatedPortfolio.getWishListCount(), 100);
    }

    @Test
    public void 동시에_100개_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    portfolioService.addWishListCount(1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        Portfolio updatedPortfolio = portfolioRepository.findById(portfolio.getId()).orElseThrow();
        assertEquals(updatedPortfolio.getWishListCount(), 100);
    }
}
