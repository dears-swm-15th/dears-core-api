package com.example.demo.chat.mapper;

import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.dto.ChatRoomDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

public interface ChatRoomMapper {
    com.example.demo.chat.mapper.ChatRoomMapper INSTANCE = Mappers.getMapper(com.example.demo.chat.mapper.ChatRoomMapper.class);

    @Mapping(target = "id", ignore = true)
    ChatRoom requestToEntity(ChatRoomDTO.Request chatRoomRequest);

    ChatRoomDTO.Response entityToResponse(ChatRoom chatRoom);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ChatRoom updateFromRequest(ChatRoomDTO.Request chatRoomRequest, @MappingTarget ChatRoom chatRoom);

}
