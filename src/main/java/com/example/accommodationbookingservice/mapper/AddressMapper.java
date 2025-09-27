package com.example.accommodationbookingservice.mapper;

import com.example.accommodationbookingservice.config.MapperConfig;
import com.example.accommodationbookingservice.dto.address.AddressDto;
import com.example.accommodationbookingservice.dto.address.AddressRequestDto;
import com.example.accommodationbookingservice.entity.accommodation.Address;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface AddressMapper {

    Address toEntity(AddressRequestDto dto);

    AddressDto toDto(Address entity);

}
