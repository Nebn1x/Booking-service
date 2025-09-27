package com.example.accommodationbookingservice.dto.bookingdto;

import com.example.accommodationbookingservice.dto.accommodation.AccommodationDto;
import com.example.accommodationbookingservice.dto.bookingstatusdto.BookingStatusDto;
import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDto {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createAt;
    private AccommodationDto accommodation;
    private UserResponseDto user;
    private BookingStatusDto status;
}
