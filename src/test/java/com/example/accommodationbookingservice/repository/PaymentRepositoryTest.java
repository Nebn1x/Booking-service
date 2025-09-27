package com.example.accommodationbookingservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.accommodationbookingservice.entity.payment.Payment;
import com.example.accommodationbookingservice.entity.payment.PaymentStatus;
import com.example.accommodationbookingservice.testutil.PaymentUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepositoryTest {

    private static final String DELETE_ALL_SQL = "database/delete/delete-all.sql";
    private static final String INSERT_ACCOMMODATION_SQL =
            "database/accommodation/insert-accommodation.sql";
    private static final String BOOKING_INSERT_BOOKING_SQL = "database/booking/insert-booking.sql";
    private static final String PAYMENT_INSERT_PAYMENTS_SQL =
            "database/payment/insert-payments.sql";
    private static final PaymentStatus.PaymentStatusName PAID_PAYMENT_STATUS
            = PaymentStatus.PaymentStatusName.PAID;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_ACCOMMODATION_SQL));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(BOOKING_INSERT_BOOKING_SQL));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(PAYMENT_INSERT_PAYMENTS_SQL));
        }
    }

    @Test
    @DisplayName("Should return payment when booking ID exists")
    void findPaymentByBookingId_ExistingBookingId_ShouldReturnPayment() {
        Payment expectedPayment = PaymentUtil.getExpectedPayment();

        Optional<Payment> actualResult = paymentRepository.findPaymentByBookingId(1L);
        assertTrue(actualResult.isPresent());

        Payment actualPayment = actualResult.get();

        assertEquals(expectedPayment.getId(), actualPayment.getId());
        assertEquals(expectedPayment.getSessionId(), actualPayment.getSessionId());
        assertEquals(expectedPayment.getSessionUrl(), actualPayment.getSessionUrl());
        assertEquals(expectedPayment.getAmount(), actualPayment.getAmount());
    }

    @Test
    @DisplayName("Should return payment by session ID")
    void findPaymentBySessionId_ExistingSessionId_ShouldReturnPayment() {
        Payment expectedPayment = PaymentUtil.getExpectedPayment();
        String sessionId = "testSessionId1";

        Optional<Payment> actualResult = paymentRepository.findPaymentBySessionId(sessionId);
        assertTrue(actualResult.isPresent());

        Payment actualPayment = actualResult.get();

        assertEquals(expectedPayment.getId(), actualPayment.getId());
        assertEquals(expectedPayment.getSessionId(), actualPayment.getSessionId());
        assertEquals(expectedPayment.getSessionUrl(), actualPayment.getSessionUrl());
        assertEquals(expectedPayment.getAmount(), actualPayment.getAmount());
    }

    @Test
    @DisplayName("Should return list of payments by user email")
    void findPaymentByBookingUserEmail_ValidEmail_ShouldReturnPayments() {
        String userEmail = "customer@example.com";
        int expectedPayments = 2;

        List<Payment> bookingUserEmail = paymentRepository.findPaymentByBookingUserEmail(userEmail);

        assertEquals(expectedPayments, bookingUserEmail.size());
    }

    @Test
    @DisplayName("Should return payments by booking IDs and payment status")
    void findAllByBookingIdInAndStatusName_ValidInput_ShouldReturnPayments() {
        List<Long> existingBookingIds = List.of(1L, 2L);

        List<Payment> actualResult = paymentRepository
                .findAllByBookingIdInAndStatus_Name(existingBookingIds, PAID_PAYMENT_STATUS);

        assertEquals(1, actualResult.size());
        assertEquals(2, actualResult.getFirst().getBooking().getId());
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(DELETE_ALL_SQL));
        }
    }
}
