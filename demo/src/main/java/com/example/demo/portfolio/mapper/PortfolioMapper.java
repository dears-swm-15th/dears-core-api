package com.example.demo.portfolio.mapper;

import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PortfolioMapper {
    PortfolioMapper INSTANCE = Mappers.getMapper(PortfolioMapper.class);

    PortfolioDTO.Response entityToResponse(Portfolio portfolio);
    @Mapping(target = "id", ignore = true)
    Portfolio requestToEntity(PortfolioDTO.Request portfolioRequest);
}