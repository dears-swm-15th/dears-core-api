package com.example.demo.portfolio;

import com.example.demo.enums.OfficeHours;
import com.example.demo.enums.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public List<PortfolioDTO.Response> getAllPortfolios() {
        return portfolioRepository.findAll().stream()
                .map(Portfolio::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<PortfolioDTO.Response> getPortfolioById(Long id) {
        return portfolioRepository.findById(id)
                .map(Portfolio::convertToResponse);
    }

    public PortfolioDTO.Response createPortfolio(PortfolioDTO.Request portfolioRequest) {
        Portfolio portfolio = Portfolio.convertToEntity(portfolioRequest);
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        return Portfolio.convertToResponse(savedPortfolio);
    }

    public PortfolioDTO.Response updatePortfolio(Long id, PortfolioDTO.Request portfolioRequest) {
        Portfolio existingPortfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        Portfolio updatedPortfolio = Portfolio.builder()
                .id(existingPortfolio.getId())
                .organization(portfolioRequest.getOrganization())
                .region(Region.valueOf(portfolioRequest.getRegion()))
                .introduction(portfolioRequest.getIntroduction())
                .officeHours(OfficeHours.valueOf(portfolioRequest.getOfficeHours()))
                .contactInfo(portfolioRequest.getContactInfo())
                .image(portfolioRequest.getImage())
                .consultationFee(portfolioRequest.getConsultationFee())
                .description(portfolioRequest.getDescription())
                .weddingPhotos(portfolioRequest.getWeddingPhotos())
                .build();

        Portfolio savedPortfolio = portfolioRepository.save(updatedPortfolio);
        return Portfolio.convertToResponse(savedPortfolio);
    }

    public void deletePortfolio(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        portfolioRepository.delete(portfolio);
    }
}
