package com.example.demo.member.mapper;

import com.example.demo.member.domain.WeddingPlanner;
import com.example.demo.member.dto.AuthDTO;
import com.example.demo.member.dto.MypageDTO;
import com.example.demo.member.dto.WeddingPlannerPortfolioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")

public interface WeddingPlannerMapper {

    WeddingPlannerMapper INSTANCE = Mappers.getMapper(WeddingPlannerMapper.class);

    @Mapping(target = "id", ignore = true)
    WeddingPlanner requestToEntity(AuthDTO.Request weddingPlannerRequest);

    public AuthDTO.Response entityToAuthDTOResponse(WeddingPlanner weddingPlanner);

    public MypageDTO.WeddingPlannerResponse entityToMypageDTOResponse(WeddingPlanner weddingPlanner);

    public WeddingPlanner weddingPlannerPortfolioResponseToEntity(WeddingPlannerPortfolioDTO.Response weddingPlannerPortfolioResponse);
}
