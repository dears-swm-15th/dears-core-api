package com.example.demo.review.mapper;

import com.example.demo.review.domain.Review;
import com.example.demo.review.dto.ReviewDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    com.example.demo.review.mapper.ReviewMapper INSTANCE = Mappers.getMapper(com.example.demo.review.mapper.ReviewMapper.class);

    @Mapping(target = "id", ignore = true)
    Review requestToEntity(ReviewDTO.Request reviewRequest);

    ReviewDTO.Response entityToResponse(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Review updateFromRequest(ReviewDTO.Request reviewRequest, @MappingTarget Review review);
}