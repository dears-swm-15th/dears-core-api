package com.teamdears.core.member.mapper;

import com.teamdears.core.member.domain.WeddingPlanner;
import com.teamdears.core.member.dto.AuthDTO;
import com.teamdears.core.member.dto.MypageDTO;
import com.teamdears.core.member.dto.WeddingPlannerPortfolioDTO;
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

    public WeddingPlannerPortfolioDTO.Response entityToWeddingPlannerPortfolioDTOResponse(WeddingPlanner weddingPlanner);

    MypageDTO.MyPageUpdateResponse entityToMypageUpdateResponse(WeddingPlanner weddingPlanner);
}
