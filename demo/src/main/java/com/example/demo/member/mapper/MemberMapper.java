package com.example.demo.member.mapper;

import com.example.demo.member.domain.Member;
import com.example.demo.member.dto.MemberAuthDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(target = "id", ignore = true)
    Member requestToEntity(MemberAuthDTO.Request memberAuthRequest);

    MemberAuthDTO.Response entityToResponse(Member member);

}
