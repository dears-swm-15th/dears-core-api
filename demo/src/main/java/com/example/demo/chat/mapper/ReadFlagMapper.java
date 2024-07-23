package com.example.demo.chat.mapper;

import com.example.demo.chat.domain.Message;
import com.example.demo.chat.domain.ReadFlag;
import com.example.demo.chat.dto.MessageDTO;
import com.example.demo.chat.dto.ReadFlagDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReadFlagMapper {
    com.example.demo.chat.mapper.ReadFlagMapper INSTANCE = Mappers.getMapper(com.example.demo.chat.mapper.ReadFlagMapper.class);

    @Mapping(target = "id", ignore = true)
    ReadFlag requestToEntity(ReadFlagDTO.Request readFlagRequest);

    ReadFlagDTO.Response entityToResponse(ReadFlag readFlag);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ReadFlag updateFromRequest(ReadFlagDTO.Request readFlagRequest, @MappingTarget ReadFlag readFlag);

}