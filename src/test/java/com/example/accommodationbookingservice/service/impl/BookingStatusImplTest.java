package com.example.accommodationbookingservice.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.example.accommodationbookingservice.dto.bookingstatusdto.BookingStatusDto;
import com.example.accommodationbookingservice.entity.booking.BookingStatus;
import com.example.accommodationbookingservice.mapper.BookingStatusMapper;
import com.example.accommodationbookingservice.repository.BookingStatusRepository;
import com.example.accommodationbookingservice.testutil.BookingStatusUtil;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingStatusImplTest {

    @Mock
    private BookingStatusRepository bookingStatusRepository;
    @Mock
    private BookingStatusMapper bookingStatusMapper;
    @InjectMocks
    private BookingStatusImpl bookingStatusService;

    private List<BookingStatusDto> bookingStatusDtoList;
    private List<BookingStatus> bookingStatusList;
    private BookingStatus bookingStatus;
    private BookingStatusDto bookingStatusDto;

    @BeforeEach
    void setUp() {
        bookingStatusDtoList = List.of(BookingStatusUtil.getBookingStatusDto());
        bookingStatusList = List.of(BookingStatusUtil.getBookingStatusPending());
        bookingStatus = BookingStatusUtil.getBookingStatusPending();
        bookingStatusDto = BookingStatusUtil.getBookingStatusDto();
    }

    @Test
    @DisplayName("Should return list of all BookingStatusDto when statuses exist in repository")
    void findAll_StatusesExist_ShouldReturnListOfBookingStatusDto() {
        List<BookingStatusDto> expected = bookingStatusDtoList;
        when(bookingStatusRepository.findAll()).thenReturn(bookingStatusList);
        when(bookingStatusMapper.toDto(bookingStatus)).thenReturn(bookingStatusDto);
        List<BookingStatusDto> actualBookingStatuses = bookingStatusService.findAll();
        assertThat(actualBookingStatuses).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Should return BookingStatus when status with given name exists")
    void findByName_ExistingName_ShouldReturnBookingStatus() {
        BookingStatus.BookingStatusName expectedStatus = bookingStatus.getName();
        BookingStatus.BookingStatusName statusName = bookingStatus.getName();
        when(bookingStatusRepository.findByName(statusName)).thenReturn(Optional.of(bookingStatus));
        Optional<BookingStatus> actualStatus = bookingStatusService.findByName(statusName);
        assertTrue(actualStatus.isPresent());
        BookingStatus.BookingStatusName actualName = actualStatus.get().getName();
        assertEquals(expectedStatus, actualName);

    }

    @Test
    @DisplayName("Should return empty Optional when status with given name does not exist")
    void findByName_NonExistingName_ShouldReturnEmptyOptional() {
        BookingStatus.BookingStatusName name = BookingStatus.BookingStatusName.CANCELED;
        when(bookingStatusRepository.findByName(name)).thenReturn(Optional.empty());
        Optional<BookingStatus> actualStatus = bookingStatusService.findByName(name);
        assertTrue(actualStatus.isEmpty());
    }
}
