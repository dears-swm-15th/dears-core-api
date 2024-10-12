package com.teamdears.core.chat.mapper;

import com.teamdears.core.chat.domain.Message;
import com.teamdears.core.chat.dto.MessageDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {
        MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

        @Mapping(target = "id", ignore = true)
        Message requestToEntity(MessageDTO.Request messageRequest);

        MessageDTO.Response entityToResponse(Message message);

        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        Message updateFromRequest(MessageDTO.Request messageRequest, @MappingTarget Message message);

}
