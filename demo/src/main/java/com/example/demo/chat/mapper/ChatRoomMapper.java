package com.example.demo.chat.mapper;

import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.dto.ChatRoomDTO;
import com.example.demo.chat.dto.ChatRoomOverviewDTO;
import com.example.demo.portfolio.domain.Portfolio;
import com.example.demo.portfolio.dto.PortfolioOverviewDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {
    com.example.demo.chat.mapper.ChatRoomMapper INSTANCE = Mappers.getMapper(com.example.demo.chat.mapper.ChatRoomMapper.class);

    @Mapping(target = "id", ignore = true)
    ChatRoom requestToEntity(ChatRoomDTO.Request chatRoomRequest);

    ChatRoomDTO.Response entityToResponse(ChatRoom chatRoom);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ChatRoom updateFromRequest(ChatRoomDTO.Request chatRoomRequest, @MappingTarget ChatRoom chatRoom);

    ChatRoomOverviewDTO.Response entityToOverviewResponse(ChatRoom chatRoom);


}
