package com.example.demo.portfolio;

import com.example.demo.config.S3Config;
import com.example.demo.config.S3Uploader;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioSearchDTO;
import com.example.demo.portfolio.repository.PortfolioRepository;
import com.example.demo.portfolio.service.PortfolioSearchService;
import com.example.demo.portfolio.service.PortfolioService;
import com.example.demo.review.dto.ReviewDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class PortfolioSearchTest {

    @MockBean
    private S3Uploader s3Uploader;

    @MockBean
    private S3Config s3Config;


    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioSearchService portfolioSearchService;

    @Autowired
    private PortfolioService portfolioService;


    @Test
    @Transactional
    public void 포트폴리오_입력() {
        Portfolio portfolio1 = portfolioRepository.findById(1L).orElseThrow();
        portfolioSearchService.indexDocumentUsingDTO(portfolio1);

        Portfolio portfolio2 = portfolioRepository.findById(2L).orElseThrow();
        portfolioSearchService.indexDocumentUsingDTO(portfolio2);

    }

    @Test
    public void 포트폴리오_검색() {
        List<PortfolioSearchDTO.Response> searchResult = portfolioSearchService.search("f");
        System.out.println("SEARCH: "+searchResult);

    }

    @Test
    @Transactional
    public void 포트폴리오_수정() {
        Portfolio portfolio = portfolioRepository.findById(1L).orElseThrow();
        portfolio.setOrganization("에바웨딩스");
        portfolioSearchService.updateDocumentUsingDTO(portfolio);
    }

    @Test
    @Transactional
    public void 포트폴리오_삭제() {
        Portfolio portfolio = portfolioRepository.findById(1L).orElseThrow();
        portfolioSearchService.deleteDocumentById(portfolio.getId());
    }

    @Test
    @Transactional
    public void 포트폴리오_리뷰_추가() {
        Portfolio portfolio = portfolioRepository.findById(1L).orElseThrow();
        ReviewDTO.Request reviewReq = ReviewDTO.Request.builder()
                .reviewerName("김민영")
                .portfolioId(1L)
                .isProvided(false)
                .content("잘해줍니다.")
                .estimate(10)
                .build();

        portfolioService.reflectNewReview(reviewReq);
    }
}
