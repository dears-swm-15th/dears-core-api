package com.teamdears.core.portfolio.mapper;

import com.teamdears.core.portfolio.domain.Portfolio;
import com.teamdears.core.portfolio.dto.PortfolioDTO;
import com.teamdears.core.portfolio.dto.PortfolioOverviewDTO;
import com.teamdears.core.portfolio.dto.PortfolioSearchDTO;
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

    PortfolioSearchDTO.Response entityToSearchResponse(Portfolio portfolio);
}