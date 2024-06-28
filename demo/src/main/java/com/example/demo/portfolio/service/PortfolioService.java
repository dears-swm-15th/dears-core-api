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
import java.util.NoSuchElementException;
import java.util.Optional;
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

        Portfolio portfolio = portfolioMapper.requestToEntity(portfolioRequest);

        Portfolio updatedPortfolio = Portfolio.builder()
                .id(existingPortfolio.getId())
                .organization(portfolio.getOrganization())
                .region(Region.valueOf(String.valueOf(portfolio.getRegion())))
                .introduction(portfolio.getIntroduction())
                .officeHours(OfficeHours.valueOf(String.valueOf(portfolio.getOfficeHours())))
                .contactInfo(portfolio.getContactInfo())
                .image(portfolio.getImage())
                .consultationFee(portfolio.getConsultationFee())
                .description(portfolio.getDescription())
                .weddingPhotos(portfolio.getWeddingPhotos())
                .build();

        Portfolio savedPortfolio = portfolioRepository.save(updatedPortfolio);
        return portfolioMapper.entityToResponse(savedPortfolio);
    }

    public void deletePortfolio(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        portfolioRepository.delete(portfolio);
    }
}
