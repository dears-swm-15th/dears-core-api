package com.teamdears.core.chat.mapper;

import com.teamdears.core.chat.domain.ChatRoom;
import com.teamdears.core.chat.dto.ChatRoomDTO;
import com.teamdears.core.chat.dto.ChatRoomOverviewDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {
    ChatRoomMapper INSTANCE = Mappers.getMapper(ChatRoomMapper.class);

    @Mapping(target = "id", ignore = true)
    ChatRoom requestToEntity(ChatRoomDTO.Request chatRoomRequest);

    ChatRoomDTO.Response entityToResponse(ChatRoom chatRoom);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ChatRoom updateFromRequest(ChatRoomDTO.Request chatRoomRequest, @MappingTarget ChatRoom chatRoom);

    ChatRoomOverviewDTO.Response entityToOverviewResponse(ChatRoom chatRoom);


}
