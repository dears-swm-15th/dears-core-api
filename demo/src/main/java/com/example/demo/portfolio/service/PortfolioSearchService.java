package com.example.demo.portfolio.service;

import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioSearchDTO;
import com.example.demo.portfolio.mapper.PortfolioMapper;
import com.example.demo.wishlist.service.WishListService;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch.core.*;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PortfolioSearchService {

    private final OpenSearchClient openSearchClient;
    private static final String indexName = "portfolio";
    private final PortfolioMapper portfolioMapper;
    private final WishListService wishListService;

    public PortfolioSearchService(OpenSearchClient openSearchClient, PortfolioMapper portfolioMapper, WishListService wishListService) {
        this.openSearchClient = openSearchClient;
        this.portfolioMapper = portfolioMapper;
        this.wishListService = wishListService;
    }

    // 인덱스 생성
    public void createIndex() {
        try {
            log.info("Creating index: {}", indexName);
            CreateIndexRequest request = CreateIndexRequest.of(builder -> builder.index(indexName));
            openSearchClient.indices().create(request);
            log.info("Index created: {}", indexName);
        } catch (Exception e) {
            log.error("Error creating index: {}", indexName, e);
        }
    }

    public void indexDocumentUsingDTO(Portfolio portfolio) {
        PortfolioSearchDTO.Request request = portfolioMapper.entityToSearchRequest(portfolio);
        try {
            log.info("Indexing document for portfolio ID: {}", portfolio.getId());
            IndexRequest<PortfolioSearchDTO.Request> indexRequest = IndexRequest.of(builder ->
                    builder.index(indexName)
                            .id(String.valueOf(request.getId()))
                            .document(request)
            );
            IndexResponse response = openSearchClient.index(indexRequest);
            log.info("Document indexed for portfolio ID: {}", portfolio.getId());
        } catch (Exception e) {
            log.error("Error indexing document for portfolio ID: {}", portfolio.getId(), e);
        }
    }

    public void updateDocumentUsingDTO(Portfolio portfolio) {
        PortfolioSearchDTO.Request request = portfolioMapper.entityToSearchRequest(portfolio);
        try {
            log.info("Updating document for portfolio ID: {}", portfolio.getId());
            UpdateRequest<PortfolioSearchDTO.Request, Object> updateRequest = UpdateRequest.of(builder ->
                    builder.index(indexName)
                            .id(String.valueOf(portfolio.getId()))
                            .doc(request)
            );

            UpdateResponse updateResponse = openSearchClient.update(updateRequest, PortfolioSearchDTO.Request.class);
            log.info("Document updated for portfolio ID: {}", portfolio.getId());
        } catch (Exception e) {
            log.error("Error updating document for portfolio ID: {}", portfolio.getId(), e);
        }
    }

    public List<PortfolioSearchDTO.Response> search(String keyword) {
        List<PortfolioSearchDTO.Response> resultList = new ArrayList<>();
        try {
            log.info("Searching documents with keyword: {}", keyword);
            List<String> sourceList = List.of("services", "plannerName", "organization", "introduction");
            SearchRequest request = buildSearchRequest(indexName, keyword, sourceList);

            SearchResponse<PortfolioSearchDTO.Request> response = openSearchClient.search(request, PortfolioSearchDTO.Request.class);
            List<Hit<PortfolioSearchDTO.Request>> hits = response.hits().hits();
            for (Hit<PortfolioSearchDTO.Request> hit : hits) {
                PortfolioSearchDTO.Response searchResponse = portfolioMapper.requestToSearchResponse(hit.source());
                searchResponse.setIsWishListed(wishListService.isWishListed(searchResponse.getId()));
                resultList.add(searchResponse);
            }
            log.info("Found {} documents with keyword: {}", resultList.size(), keyword);
        } catch (Exception e) {
            log.error("Error searching documents with keyword: {}", keyword, e);
        }
        return resultList;
    }

    public SearchRequest buildSearchRequest(String indexName, String keyword, List<String> sourceList) {
        return SearchRequest.of(searchRequest ->
                searchRequest.index(indexName)
                        .query(query ->
                                query.bool(bool -> {
                                    addShouldClauses(bool, keyword, sourceList);
                                    return bool;
                                })
                        )
        );
    }

    private void addShouldClauses(BoolQuery.Builder bool, String keyword, List<String> fields) {
        for (String field : fields) {
            bool.should(should ->
                    should.wildcard(wildcard ->
                            wildcard.field(field).value("*" + keyword + "*")
                    )
            );
        }
    }

    public void deleteDocumentById(Long id) {
        try {
            log.info("Deleting document by ID: {}", id);
            DeleteRequest deleteRequest = DeleteRequest.of(builder ->
                    builder.index(indexName)
                            .id(String.valueOf(id))
            );
            DeleteResponse response = openSearchClient.delete(deleteRequest);
            log.info("Document deleted by ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting document by ID: {}", id, e);
        }
    }
}
