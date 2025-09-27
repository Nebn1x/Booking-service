package com.example.accommodationbookingservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.accommodationbookingservice.entity.booking.Booking;
import com.example.accommodationbookingservice.testutil.BookingUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookingRepositoryTest {

    public static final String DATABASE_DELETE_DELETE_ALL_SQL = "database/delete/delete-all.sql";
    public static final String BOOKING_INSERT_BOOKING_SQL = "database/booking/insert-booking.sql";
    public static final String INSERT_ACCOMMODATION_SQL =
            "database/accommodation/insert-accommodation.sql";
    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_ACCOMMODATION_SQL));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(BOOKING_INSERT_BOOKING_SQL));
        }
    }

    @Test
    @DisplayName("Should return count of overlapping bookings excluding canceled and expired")
    void isDatesAvailableForAccommodation_ValidDatesAndStatus_ShouldReturnCount() {
        Long accommodationId = 1L;
        Booking expectedBooking = BookingUtil.getExpectedBooking();

        Long datesAvailableForAccommodation = bookingRepository
                .isDatesAvailableForAccommodation(accommodationId, expectedBooking.getCheckInDate(),
                        expectedBooking.getCheckOutDate());

        Long expectedResult = 1L;
        assertEquals(expectedResult, datesAvailableForAccommodation);
    }

    @Test
    @DisplayName("Should return paginated list of bookings for given user email")
    void findAllByUserEmail_ValidEmailAndPageable_ShouldReturnBookings() {
        String email = "customer@example.com";
        Pageable pageable = Pageable.ofSize(1);
        Booking expectedBooking = BookingUtil.getExpectedBooking();

        List<Booking> allByUserEmail =
                bookingRepository.findAllByUser_Email(email, pageable);
        Booking actualBooking = allByUserEmail.getFirst();

        assertEquals(expectedBooking.getCheckInDate(), actualBooking.getCheckInDate());
        assertEquals(expectedBooking.getCheckOutDate(), actualBooking.getCheckOutDate());
        assertEquals(expectedBooking.getBookingCreatedAt(), actualBooking.getBookingCreatedAt());
    }

    @Test
    @DisplayName("Should return all bookings by user ID")
    void findByUserId_ValidId_ShouldReturnBookings() {
        Long id = 1L;
        Booking expectedBooking = BookingUtil.getExpectedBooking();

        List<Booking> allByUserEmail =
                bookingRepository.findByUserId(id);
        Booking actualBooking = allByUserEmail.getFirst();

        assertEquals(expectedBooking.getCheckInDate(), actualBooking.getCheckInDate());
        assertEquals(expectedBooking.getCheckOutDate(), actualBooking.getCheckOutDate());
        assertEquals(expectedBooking.getBookingCreatedAt(), actualBooking.getBookingCreatedAt());
    }

    @Test
    @DisplayName("Should return bookings by check-in date and status")
    void findAllByCheckInDateAndStatus_ValidDateAndStatus_ShouldReturnBookings() {
        Booking expectedBooking = BookingUtil.getExpectedBooking();

        List<Booking> actualResult = bookingRepository
                .findAllByCheckInDate(expectedBooking.getCheckInDate(),
                        expectedBooking.getStatus().getName());

        Booking actualBooking = actualResult.getFirst();

        assertEquals(expectedBooking.getCheckInDate(), actualBooking.getCheckInDate());
        assertEquals(expectedBooking.getCheckOutDate(), actualBooking.getCheckOutDate());
        assertEquals(expectedBooking.getBookingCreatedAt(), actualBooking.getBookingCreatedAt());
        assertEquals(expectedBooking.getStatus().getName(), actualBooking.getStatus().getName());
    }

    @Test
    @DisplayName("Should return bookings created before specific date and with given status")
    void findAllByCreatedAtAndStatus_BeforeDateWithStatus_ShouldReturnBookings() {
        Booking expectedBooking = BookingUtil.getExpectedBooking();

        List<Booking> actualResult = bookingRepository
                .findByCheckOutDateAndStatus(expectedBooking.getCheckOutDate(),
                        expectedBooking.getStatus().getName());

        Booking actualBooking = actualResult.getFirst();

        assertEquals(expectedBooking.getCheckInDate(), actualBooking.getCheckInDate());
        assertEquals(expectedBooking.getCheckOutDate(), actualBooking.getCheckOutDate());
        assertEquals(expectedBooking.getBookingCreatedAt(), actualBooking.getBookingCreatedAt());
        assertEquals(expectedBooking.getStatus().getName(), actualBooking.getStatus().getName());
    }

    @Test
    @DisplayName("Should return bookings by check-out date and status")
    void findByCheckOutDateAndStatus_ValidDateAndStatus_ShouldReturnBookings() {
        Booking expectedBooking = BookingUtil.getExpectedBooking();

        List<Booking> actualResult = bookingRepository
                .findByCheckOutDateAndStatus(expectedBooking.getCheckOutDate(),
                        expectedBooking.getStatus().getName());

        Booking actualBooking = actualResult.getFirst();

        assertEquals(expectedBooking.getCheckInDate(), actualBooking.getCheckInDate());
        assertEquals(expectedBooking.getCheckOutDate(), actualBooking.getCheckOutDate());
        assertEquals(expectedBooking.getBookingCreatedAt(), actualBooking.getBookingCreatedAt());
        assertEquals(expectedBooking.getStatus().getName(), actualBooking.getStatus().getName());
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(DATABASE_DELETE_DELETE_ALL_SQL));
        }
    }
}
