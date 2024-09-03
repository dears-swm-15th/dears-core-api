package com.example.demo.portfolio.mapper;

import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioDTO;
import com.example.demo.portfolio.dto.PortfolioOverviewDTO;
import com.example.demo.portfolio.dto.PortfolioSearchDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PortfolioMapper {
    PortfolioMapper INSTANCE = Mappers.getMapper(PortfolioMapper.class);

    @Mapping(target = "id", ignore = true)
    Portfolio requestToEntity(PortfolioDTO.Request portfolioRequest);

    PortfolioDTO.Response entityToResponse(Portfolio portfolio);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Portfolio updateFromRequest(PortfolioDTO.Request portfolioRequest, @MappingTarget Portfolio portfolio);

    PortfolioOverviewDTO.Response entityToOverviewResponse(Portfolio portfolio);

    PortfolioSearchDTO.Request entityToSearchRequest(Portfolio portfolio);

    PortfolioSearchDTO.Response requestToSearchResponse(PortfolioSearchDTO.Request request);


}