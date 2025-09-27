package com.example.accommodationbookingservice.mapper;

import com.example.accommodationbookingservice.config.MapperConfig;
import com.example.accommodationbookingservice.dto.accommodation.AccommodationDto;
import com.example.accommodationbookingservice.dto.accommodation.AccommodationRequestDto;
import com.example.accommodationbookingservice.entity.accommodation.Accommodation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface AccommodationMapper {

    @Mapping(source = "size", target = "sizeType")
    AccommodationDto toDto(Accommodation accommodation);

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "size", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "address", ignore = true)
    Accommodation toModelWithoutAddressAndTypes(AccommodationRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    void updateFromDto(AccommodationRequestDto dto, @MappingTarget Accommodation accommodation);

}
