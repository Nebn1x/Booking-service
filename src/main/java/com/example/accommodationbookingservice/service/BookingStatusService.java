package com.example.accommodationbookingservice.service;

import com.example.accommodationbookingservice.dto.bookingstatusdto.BookingStatusDto;
import com.example.accommodationbookingservice.entity.booking.BookingStatus;
import java.util.List;
import java.util.Optional;

public interface BookingStatusService {

    List<BookingStatusDto> findAll();

    Optional<BookingStatus> findByName(BookingStatus.BookingStatusName name);
}
