package com.example.demo.portfolio.mapper;

import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PortfolioMapper {
    PortfolioMapper INSTANCE = Mappers.getMapper(PortfolioMapper.class);

    PortfolioDTO entityToDTO(Portfolio portfolio);
    Portfolio DTOToEntity(PortfolioDTO portfolioDTO);
}