package com.example.demo.portfolio;

import com.example.demo.config.OpenSearchConfig;
import com.example.demo.config.S3Config;
import com.example.demo.config.S3Uploader;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.repository.PortfolioRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest //JPA Repository들에 대한 빈 등록하여 테스트 가능
@DisplayName("Portfolio Test")
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PortfolioRepositoryTest {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @MockBean
    private S3Uploader s3Uploader;

    @MockBean
    private S3Config s3Config;

    @MockBean
    private OpenSearchConfig openSearchConfig;

    @MockBean
    private OpenSearchClient openSearchClient;

    @Before
    public void setUp() {
    }

    @Test
    @DisplayName("Pagination을 활용하여 포트폴리오 리스트를 조회")
    void findAllByPage() {
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by("id").ascending());

        Page<Portfolio> portfolios = portfolioRepository.findAll(pageRequest);

        //junit
        Assertions.assertThat(portfolios.getSize()).isEqualTo(1);
    }
}
