package com.example.demo.chat.mapper;

import com.example.demo.chat.domain.Room;
import com.example.demo.chat.dto.RoomDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    com.example.demo.chat.mapper.RoomMapper INSTANCE = Mappers.getMapper(com.example.demo.chat.mapper.RoomMapper.class);

    @Mapping(target = "id", ignore = true)
    Room requestToEntity(RoomDTO.Request roomRequest);

    RoomDTO.Response entityToResponse(Room room);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Room updateFromRequest(RoomDTO.Request roomRequest, @MappingTarget Room room);
}