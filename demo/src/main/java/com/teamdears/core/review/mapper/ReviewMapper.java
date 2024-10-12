package com.teamdears.core.review.mapper;

import com.teamdears.core.review.domain.Review;
import com.teamdears.core.review.dto.ReviewDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(target = "id", ignore = true)
    Review requestToEntity(ReviewDTO.Request reviewRequest);

    ReviewDTO.Response entityToResponse(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Review updateFromRequest(ReviewDTO.Request reviewRequest, @MappingTarget Review review);
}