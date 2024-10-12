package com.teamdears.core.member.mapper;

import com.teamdears.core.member.domain.Customer;
import com.teamdears.core.member.dto.AuthDTO;
import com.teamdears.core.member.dto.MypageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "id", ignore = true)
    Customer requestToEntity(AuthDTO.Request memberAuthRequest);

    AuthDTO.Response entityToAuthDTOResponse(Customer customer);

    MypageDTO.CustomerResponse entityToMypageDTOResponse(Customer customer);

    MypageDTO.MyPageUpdateResponse entityToMypageUpdateResponse(Customer customer);
}
