package com.example.accommodationbookingservice.mapper;

import com.example.accommodationbookingservice.dto.bookingdto.BookingDto;
import com.example.accommodationbookingservice.dto.bookingdto.BookingRequestDto;
import com.example.accommodationbookingservice.entity.accommodation.Address;
import com.example.accommodationbookingservice.entity.booking.Booking;
import java.time.LocalDate;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {
        AccommodationMapper.class,
        UserMapper.class,
        BookingStatusMapper.class
})
public interface BookingMapper {
    String DESCRIPTION = "Booking at %s, %s, %s %s %s from %s to %s";

    @Mapping(target = "createAt", source = "bookingCreatedAt")
    BookingDto toDto(Booking booking);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "user", ignore = true)
    Booking toModelWithoutStatusAndUser(BookingRequestDto requestDto);

    void setUpdateInfoToBooking(@MappingTarget Booking booking, BookingRequestDto requestDto);

    List<BookingDto> toDtos(List<Booking> bookingList);

    default String getBookingDescription(Booking booking) {
        Address address = booking.getAccommodation().getAddress();
        LocalDate checkInDate = booking.getCheckInDate();
        LocalDate checkOutDate = booking.getCheckOutDate();
        return DESCRIPTION.formatted(address.getCountry(), address.getState(),
                address.getCity(), address.getStreet(), address.getHouseNumber(),
                checkInDate, checkOutDate);
    }
}
