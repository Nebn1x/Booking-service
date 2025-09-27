package com.example.accommodationbookingservice.mapper;

import com.example.accommodationbookingservice.config.MapperConfig;
import com.example.accommodationbookingservice.dto.bookingstatusdto.BookingStatusDto;
import com.example.accommodationbookingservice.entity.booking.BookingStatus;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookingStatusMapper {
    BookingStatusDto toDto(BookingStatus bookingStatus);

}
