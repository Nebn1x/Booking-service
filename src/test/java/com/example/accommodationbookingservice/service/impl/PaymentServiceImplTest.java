package com.example.accommodationbookingservice.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.accommodationbookingservice.dto.bookingdto.BookingDto;
import com.example.accommodationbookingservice.dto.payment.PaymentDetailsDto;
import com.example.accommodationbookingservice.dto.payment.PaymentDto;
import com.example.accommodationbookingservice.entity.booking.Booking;
import com.example.accommodationbookingservice.entity.booking.BookingStatus;
import com.example.accommodationbookingservice.entity.payment.Payment;
import com.example.accommodationbookingservice.entity.payment.PaymentStatus;
import com.example.accommodationbookingservice.exception.BookingPaymentNotAllowedException;
import com.example.accommodationbookingservice.exception.EntityNotFoundException;
import com.example.accommodationbookingservice.exception.UnauthorizedBookingAccessException;
import com.example.accommodationbookingservice.mapper.PaymentMapper;
import com.example.accommodationbookingservice.repository.BookingRepository;
import com.example.accommodationbookingservice.repository.BookingStatusRepository;
import com.example.accommodationbookingservice.repository.PaymentRepository;
import com.example.accommodationbookingservice.repository.PaymentStatusRepository;
import com.example.accommodationbookingservice.telegram.BookingNotificationBot;
import com.example.accommodationbookingservice.testutil.BookingStatusUtil;
import com.example.accommodationbookingservice.testutil.BookingUtil;
import com.example.accommodationbookingservice.testutil.PaymentStatusUtil;
import com.example.accommodationbookingservice.testutil.PaymentUtil;
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
class PaymentServiceImplTest {

    @Mock
    private BookingNotificationBot bookingNotificationBot;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingStatusRepository bookingStatusRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentStatusRepository paymentStatusRepository;
    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Booking booking;
    private Booking canceledBooking;
    private BookingDto bookingDto;
    private BookingStatus bookingStatusPending;
    private PaymentDetailsDto paymentDetailsDto;
    private Payment payment;
    private PaymentDto paymentDto;
    private PaymentStatus paidPaymentStatus;

    @BeforeEach
    void setUp() {
        booking = BookingUtil.getPendingBooking();
        bookingDto = BookingUtil.getBookingDto();
        canceledBooking = BookingUtil.getCanceledBooking();
        bookingStatusPending = BookingStatusUtil.getBookingStatusPending();
        payment = PaymentUtil.getPayment();
        paymentDto = PaymentUtil.getPaymentDto();
        paidPaymentStatus = PaymentStatusUtil.getPaymentStatusPaid();
        paymentDetailsDto = PaymentUtil.getPaymentDetailsDto();
    }

    @Test
    @DisplayName("Should create Stripe session and return "
            + "payment URL for valid booking and user email")
    void createPaymentCheckoutSession_ValidBookingAndUser_ShouldCreateSessionAndReturnUrl() {
        Long id = 1L;
        String email = "test@gmail.com";
        String expectedPaymentCheckoutSession = "/session-test-payment-url";

        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));
        when(paymentRepository.findPaymentByBookingId(id)).thenReturn(Optional.of(payment));

        String actualPaymentCheckoutSession = paymentService
                .createPaymentCheckoutSession(id, email);

        assertEquals(actualPaymentCheckoutSession, expectedPaymentCheckoutSession);
    }

    @DisplayName("Should throw EntityNotFoundException when booking with given ID does not exist")
    @Test
    void createPaymentCheckoutSession_BookingNotFound_ShouldThrowException() {
        Long id = 999L;
        String email = "test@gmail.com";

        EntityNotFoundException actualException = assertThrows(EntityNotFoundException.class,
                () -> paymentService.createPaymentCheckoutSession(id, email));

        String expectedMessage = "Cant find booking id " + id;

        assertEquals(expectedMessage, actualException.getMessage());
    }

    @DisplayName("Should throw BookingPaymentNotAllowedException "
            + "when booking status is not PENDING")
    @Test
    void createPaymentCheckoutSession_StatusNotPending_ShouldThrowException() {
        Long id = 1L;
        String email = "test@gmail.com";

        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));
        booking.setStatus(BookingStatusUtil.getBookingStatusCanceled());

        BookingPaymentNotAllowedException actualException =
                assertThrows(BookingPaymentNotAllowedException.class, ()
                        -> paymentService.createPaymentCheckoutSession(id, email));

        String expectedMessage = "Cant paid booking id "
                + id
                + " because booking status " + booking.getStatus().getName();

        assertEquals(expectedMessage, actualException.getMessage());
    }

    @DisplayName("Should throw UnauthorizedBookingAccessException "
            + "when email does not match booking user")
    @Test
    void createPaymentCheckoutSession_EmailMismatch_ShouldThrowException() {
        Long id = 1L;
        String email = "wrong@gmail.com";

        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));

        UnauthorizedBookingAccessException actualException =
                assertThrows(UnauthorizedBookingAccessException.class,
                        () -> paymentService.createPaymentCheckoutSession(id, email));

        String expectedMessage = "User with email "
                + email
                + " cant have permission to booking with id. "
                + id;

        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should confirm payment, update statuses and return "
            + "PaymentDto when Stripe session is paid")
    void successPayment_ValidStripeExistingSession_ShouldUpdateStatusesAndReturnPaymentDto() {
        final PaymentDto expectedPaymentDto = paymentDto;
        payment.setStatus(paidPaymentStatus);

        when(paymentRepository.findPaymentBySessionId(payment.getSessionId()))
                .thenReturn(Optional.of(payment));
        when(paymentMapper.toDto(payment)).thenReturn(paymentDto);

        PaymentDto paymentDto = paymentService.successPayment(payment.getSessionId());
        assertEquals(expectedPaymentDto, paymentDto);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException "
            + "when payment by session id not found")
    void successPayment_SessionNotPaid_ShouldThrowException() {
        String session = "test session";

        EntityNotFoundException actualException = assertThrows(EntityNotFoundException.class,
                () -> paymentService.successPayment(session));

        String expectedMessage = "Cant find payment where session id. " + session;

        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should return list of all PaymentDetailsDto from repository")
    void findAllPayments_ExistingPayments_ShouldReturnListOfPaymentDetailsDto() {
        List<PaymentDetailsDto> expected = List.of(paymentDetailsDto);
        when(paymentRepository.findAll()).thenReturn(List.of(payment));
        when(paymentMapper.toInfoDto(payment)).thenReturn(paymentDetailsDto);

        List<PaymentDetailsDto> actualPayments = paymentService.findAllPayments();

        assertThat(actualPayments).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Should return list of PaymentDetailsDto for specified user email")
    void findPaymentsByUserEmail_ValidEmail_ShouldReturnUserPayments() {
        String email = "test@gmail.com";
        List<PaymentDetailsDto> expected = List.of(paymentDetailsDto);

        when(paymentRepository.findPaymentByBookingUserEmail(email))
                .thenReturn(List.of(payment));
        when(paymentMapper.toInfoDto(payment)).thenReturn(paymentDetailsDto);

        List<PaymentDetailsDto> actualPaymentsByEmail = paymentService
                .findPaymentsByUserEmail(email);

        assertThat(actualPaymentsByEmail).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("")
    @Test
    void cancelPaymentAndBooking_BookingStatusIsNotExist_ShouldThrowException() {
        String session = "test session";

        when(paymentRepository.findPaymentBySessionId(session))
                .thenReturn(Optional.of(payment));

        EntityNotFoundException actualException = assertThrows(EntityNotFoundException.class,
                () -> paymentService.cancelPaymentAndBooking(session));

        String expectedMessage = "Can't find status by name";

        assertEquals(expectedMessage, actualException.getMessage());
    }
}
