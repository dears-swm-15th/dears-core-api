package com.example.demo.portfolio.service;

import com.example.demo.enums.OfficeHours;
import com.example.demo.enums.Region;
import com.example.demo.portfolio.mapper.PortfolioMapper;
import com.example.demo.portfolio.repository.PortfolioRepository;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper = PortfolioMapper.INSTANCE;

    public List<PortfolioDTO.Response> getAllPortfolios() {
        return portfolioRepository.findAll().stream()
                .map(portfolioMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public PortfolioDTO.Response getPortfolioById(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        return portfolioMapper.entityToResponse(portfolio);
    }

    public PortfolioDTO.Response createPortfolio(PortfolioDTO.Request portfolioRequest) {
        Portfolio portfolio = portfolioMapper.requestToEntity(portfolioRequest);
        portfolio = portfolioRepository.save(portfolio);
        return portfolioMapper.entityToResponse(portfolio);
    }

    public PortfolioDTO.Response updatePortfolio(Long id, PortfolioDTO.Request portfolioRequest) {
        Portfolio existingPortfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        Portfolio updatedPortfolio = portfolioMapper.updateFromRequest(portfolioRequest, existingPortfolio);

//        Portfolio updatedPortfolio = Portfolio.builder()
//                .id(existingPortfolio.getId())
//                .organization(portfolio.getOrganization())
//                .region(Region.valueOf(String.valueOf(portfolio.getRegion())))
//                .introduction(portfolio.getIntroduction())
//                .contactInfo(portfolio.getContactInfo())
//                .profileImageUrl(portfolio.getProfileImageUrl())
//                .consultationFee(portfolio.getConsultationFee())
//                .description(portfolio.getDescription())
//                .avgFee(portfolio.getAvgFee())
//                .minFee(portfolio.getMinFee())
//                .services(portfolio.getServices())
//                .rating(portfolio.getRating())
//                .weddingPhotoUrls(portfolio.getWeddingPhotoUrls())
//                .radar(portfolio.getRadar())
//                .build();

        Portfolio savedPortfolio = portfolioRepository.save(updatedPortfolio);
        return portfolioMapper.entityToResponse(savedPortfolio);
    }

    public void deletePortfolio(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        portfolioRepository.delete(portfolio);
    }

//    private Integer calculateAvgFee(Integer existingAvgFee, Integer existingConsultationFee, Integer newConsultationFee, Integer count) {
//        if (newConsultationFee == null) {
//            return existingAvgFee;
//        }
//        int totalFee = (existingAvgFee * count) + newConsultationFee;
//        return totalFee / (count + 1);
//    }
//
//    private Integer calculateMinFee(Integer existingMinFee, Integer newConsultationFee) {
//        if (newConsultationFee == null) {
//            return existingMinFee;
//        }
//        return (existingMinFee == null || newConsultationFee < existingMinFee) ? newConsultationFee : existingMinFee;
//    }
//
//    private Float calculateRating(Float existingRating, Float newRating, Integer count) {
//        if (newRating == null) {
//            return existingRating;
//        }
//        float totalRating = (existingRating * count) + newRating;
//        return totalRating / (count + 1);
//    }
}
