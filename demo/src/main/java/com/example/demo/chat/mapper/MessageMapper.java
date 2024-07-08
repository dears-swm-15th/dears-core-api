package com.example.demo.chat.mapper;

import com.example.demo.chat.domain.Message;
import com.example.demo.chat.dto.MessageDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    com.example.demo.chat.mapper.MessageMapper INSTANCE = Mappers.getMapper(com.example.demo.chat.mapper.MessageMapper.class);

    @Mapping(target = "id", ignore = true)
    Message requestToEntity(MessageDTO.Request messageRequest);

    MessageDTO.Response entityToResponse(Message message);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Message updateFromRequest(MessageDTO.Request messageRequest, @MappingTarget Message message);
}