package com.example.accommodationbookingservice.service.impl;

import com.example.accommodationbookingservice.dto.bookingstatusdto.BookingStatusDto;
import com.example.accommodationbookingservice.entity.booking.BookingStatus;
import com.example.accommodationbookingservice.mapper.BookingStatusMapper;
import com.example.accommodationbookingservice.repository.BookingStatusRepository;
import com.example.accommodationbookingservice.service.BookingStatusService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookingStatusImpl implements BookingStatusService {

    private final BookingStatusRepository bookingStatusRepository;
    private final BookingStatusMapper bookingStatusMapper;

    @Override
    public List<BookingStatusDto> findAll() {
        List<BookingStatus> listOfTypes = bookingStatusRepository.findAll();
        return listOfTypes.stream()
                .map(bookingStatusMapper::toDto)
                .toList();
    }

    @Override
    public Optional<BookingStatus> findByName(BookingStatus.BookingStatusName name) {
        return bookingStatusRepository.findByName(name);
    }
}
