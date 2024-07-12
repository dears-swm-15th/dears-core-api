package com.example.demo.member.mapper;

import com.example.demo.member.domain.Customer;
import com.example.demo.member.dto.AuthDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "id", ignore = true)
    Customer requestToEntity(AuthDTO.Request memberAuthRequest);

    AuthDTO.Response entityToResponse(Customer customer);

}
