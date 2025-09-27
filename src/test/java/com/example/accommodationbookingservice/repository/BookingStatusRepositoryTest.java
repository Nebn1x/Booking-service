package com.example.accommodationbookingservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.accommodationbookingservice.entity.booking.BookingStatus;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookingStatusRepositoryTest {

    private static final BookingStatus.BookingStatusName BOOKING_STATUS_NAME_PENDING =
            BookingStatus.BookingStatusName.PENDING;
    private static final BookingStatus.BookingStatusName BOOKING_STATUS_NAME_EXPIRED =
            BookingStatus.BookingStatusName.EXPIRED;
    private static final BookingStatus.BookingStatusName BOOKING_STATUS_NAME_CONFIRMED =
            BookingStatus.BookingStatusName.CONFIRMED;
    private static final BookingStatus.BookingStatusName BOOKING_STATUS_NAME_CANCELED =
            BookingStatus.BookingStatusName.CANCELED;

    @Autowired
    private BookingStatusRepository bookingStatusRepository;

    @Test
    @DisplayName("Should return BookingStatus Pending")
    void findByName_ExistingStatusPending_ShouldReturnBookingStatus() {
        Optional<BookingStatus> actualResult = bookingStatusRepository
                .findByName(BOOKING_STATUS_NAME_PENDING);
        assertTrue(actualResult.isPresent());
        BookingStatus actualBookingStatus = actualResult.get();

        assertEquals(BOOKING_STATUS_NAME_PENDING, actualBookingStatus.getName());
    }

    @Test
    @DisplayName("Should return BookingStatus Expired")
    void findByName_ExistingStatusExpired_ShouldReturnBookingStatus() {
        Optional<BookingStatus> actualResult = bookingStatusRepository
                .findByName(BOOKING_STATUS_NAME_EXPIRED);
        assertTrue(actualResult.isPresent());
        BookingStatus actualBookingStatus = actualResult.get();

        assertEquals(BOOKING_STATUS_NAME_EXPIRED, actualBookingStatus.getName());
    }

    @Test
    @DisplayName("Should return BookingStatus Confirmed")
    void findByName_ExistingStatusConfirmed_ShouldReturnBookingStatus() {
        Optional<BookingStatus> actualResult = bookingStatusRepository
                .findByName(BOOKING_STATUS_NAME_CONFIRMED);
        assertTrue(actualResult.isPresent());
        BookingStatus actualBookingStatus = actualResult.get();

        assertEquals(BOOKING_STATUS_NAME_CONFIRMED, actualBookingStatus.getName());
    }

    @Test
    @DisplayName("Should return BookingStatus Canceled")
    void findByName_ExistingStatusCanceled_ShouldReturnBookingStatus() {
        Optional<BookingStatus> actualResult = bookingStatusRepository
                .findByName(BOOKING_STATUS_NAME_CANCELED);
        assertTrue(actualResult.isPresent());
        BookingStatus actualBookingStatus = actualResult.get();

        assertEquals(BOOKING_STATUS_NAME_CANCELED, actualBookingStatus.getName());
    }
}
