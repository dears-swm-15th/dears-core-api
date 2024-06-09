package com.example.demo.service;

import com.example.demo.domain.Portfolio;
import com.example.demo.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    public Optional<Portfolio> getPortfolioById(Long id) {
        return portfolioRepository.findById(id);
    }

    public Portfolio createPortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public Portfolio updatePortfolio(Long id, Portfolio portfolioDetails) {
        Portfolio existingPortfolio = portfolioRepository.findById(id).orElseThrow(() -> new RuntimeException("Portfolio not found"));

        Portfolio updatedPortfolio = Portfolio.builder()
                .id(existingPortfolio.getId())
                .organization(portfolioDetails.getOrganization())
                .region(portfolioDetails.getRegion())
                .introduction(portfolioDetails.getIntroduction())
                .officeHours(portfolioDetails.getOfficeHours())
                .contactInfo(portfolioDetails.getContactInfo())
                .image(portfolioDetails.getImage())
                .consultationFee(portfolioDetails.getConsultationFee())
                .description(portfolioDetails.getDescription())
                .weddingPhotos(portfolioDetails.getWeddingPhotos())
                .build();

        return portfolioRepository.save(updatedPortfolio);
    }

    public void deletePortfolio(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(() -> new RuntimeException("Portfolio not found"));
        portfolioRepository.delete(portfolio);
    }
}
