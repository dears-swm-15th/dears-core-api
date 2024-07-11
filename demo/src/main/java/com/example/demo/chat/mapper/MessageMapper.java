package com.example.demo.chat.mapper;

import com.example.demo.legacychat.domain.Message;
import com.example.demo.legacychat.dto.MessageDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(target = "id", ignore = true)
    Message requestToEntity(MessageDTO.Request messageRequest);

    MessageDTO.Response entityToResponse(Message message);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Message updateFromRequest(MessageDTO.Request messageRequest, @MappingTarget Message message);
}