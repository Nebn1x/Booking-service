package com.example.accommodationbookingservice.service;

import com.example.accommodationbookingservice.dto.bookingdto.BookingDto;
import com.example.accommodationbookingservice.dto.bookingdto.BookingRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookingService {

    BookingDto save(BookingRequestDto requestDto, String email);

    List<BookingDto> findByUserEmail(String email, Pageable pageable);

    List<BookingDto> findAllBookings(Pageable pageable);

    BookingDto getById(Long id, String email);

    List<BookingDto> getBookingByUserId(Long userId);

    BookingDto updateInfo(Long id, BookingRequestDto requestDto, String email);

    BookingDto cancel(Long id, String email);
}
