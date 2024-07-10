package com.example.demo.legacychat.mapper;

import com.example.demo.legacychat.domain.Room;
import com.example.demo.legacychat.dto.RoomDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    @Mapping(target = "id", ignore = true)
    Room requestToEntity(RoomDTO.Request roomRequest);

    RoomDTO.Response entityToResponse(Room room);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Room updateFromRequest(RoomDTO.Request roomRequest, @MappingTarget Room room);
}