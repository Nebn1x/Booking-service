package com.example.accommodationbookingservice.exception.exceptionhandler;

import com.example.accommodationbookingservice.exception.BookingPaymentNotAllowedException;
import com.example.accommodationbookingservice.exception.EmailTokenGeneratorException;
import com.example.accommodationbookingservice.exception.EntityNotFoundException;
import com.example.accommodationbookingservice.exception.IncorrectPasswordException;
import com.example.accommodationbookingservice.exception.InvalidDateException;
import com.example.accommodationbookingservice.exception.InvalidPaymentStatusException;
import com.example.accommodationbookingservice.exception.InvalidReservationStatusException;
import com.example.accommodationbookingservice.exception.InvalidTelegramToken;
import com.example.accommodationbookingservice.exception.NoAvailableUnitsException;
import com.example.accommodationbookingservice.exception.PaymentNotConfirmedException;
import com.example.accommodationbookingservice.exception.PermissionException;
import com.example.accommodationbookingservice.exception.RegistrationException;
import com.example.accommodationbookingservice.exception.StripePaymentSessionException;
import com.example.accommodationbookingservice.exception.StripeSessionCancellationException;
import com.example.accommodationbookingservice.exception.TelegramBotException;
import com.example.accommodationbookingservice.exception.UnauthorizedBookingAccessException;
import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {

    private static final String ENTITY_NOT_FOUND_EXCEPTION =
            "Entity not found exception";
    private static final String TOKEN_GENERATOR_EXCEPTION =
            "Email token generator exception";
    private static final String INCORRECT_PASSWORD_EXCEPTION =
            "Incorrect password exception";
    private static final String INVALID_DATE_EXCEPTION =
            "Invalid date exception";
    private static final String INVALID_RESERVATION_STATUS_EXCEPTION =
            "Invalid reservation status exception";
    private static final String INVALID_TELEGRAM_TOKEN_EXCEPTION =
            "Invalid telegram token exception";
    private static final String NO_AVAILABLE_UNIT_EXCEPTION =
            "No available unit exception";
    private static final String PERMISSION_EXCEPTION =
            "Permission exception";
    private static final String REGISTRATION_EXCEPTION =
            "Registration exception";
    private static final String TELEGRAM_BOT_EXCEPTION =
            "Telegram bot exception";
    private static final String STRIPE_SESSION_CANCEL_EXCEPTION =
            "Stripe session cancel exception";
    private static final String STRIPE_PAYMENT_SESSION_EXCEPTION =
            "Stripe payment session exception";
    private static final String TELEGRAM_API_REQUEST_EXCEPTION =
            "Telegram api request exception";
    private static final String INVALID_PAYMENT_STATUS_EXCEPTION =
            "Invalid payment status exception";
    private static final String BOOKING_PAYMENT_NOT_ALLOWED_EXCEPTION =
            "Booking payment not allowed exception";
    private static final String UNAUTHORIZED_BOOKING_ACCESS_EXCEPTION =
            "Unauthorized booking access exception";
    private static final String PAYMENT_NOT_CONFIRM_EXCEPTION =
            "Payment not confirm exception";

    @ExceptionHandler(EmailTokenGeneratorException.class)
    public ResponseEntity<ApiError> handleEmailTokenGeneratorException(
            EmailTokenGeneratorException ex) {
        ApiError apiError = new ApiError(
                TOKEN_GENERATOR_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ApiError> handleInvalidDateException(InvalidDateException ex) {
        ApiError apiError = new ApiError(
                INVALID_DATE_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidReservationStatusException.class)
    public ResponseEntity<ApiError> handleInvalidReservationStatusException(
            InvalidReservationStatusException ex) {
        ApiError apiError = new ApiError(
                INVALID_RESERVATION_STATUS_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ApiError> handleRegistrationException(RegistrationException ex) {
        ApiError apiError = new ApiError(
                REGISTRATION_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TelegramBotException.class)
    public ResponseEntity<ApiError> handleTelegramBotException(TelegramBotException ex) {
        ApiError apiError = new ApiError(
                TELEGRAM_BOT_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentNotConfirmedException.class)
    public ResponseEntity<ApiError> handlePaymentNotConfirmException(
            PaymentNotConfirmedException ex) {
        ApiError apiError = new ApiError(
                PAYMENT_NOT_CONFIRM_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTelegramToken.class)
    public ResponseEntity<ApiError> handleInvalidTelegramToken(InvalidTelegramToken ex) {
        ApiError apiError = new ApiError(
                INVALID_TELEGRAM_TOKEN_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ApiError> handleIncorrectPasswordException(
            IncorrectPasswordException ex) {
        ApiError apiError = new ApiError(
                INCORRECT_PASSWORD_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<ApiError> handlePermissionException(PermissionException ex) {
        ApiError apiError = new ApiError(
                PERMISSION_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedBookingAccessException.class)
    public ResponseEntity<ApiError> handleUnauthorizedBookingAccessException(
            UnauthorizedBookingAccessException ex) {
        ApiError apiError = new ApiError(
                UNAUTHORIZED_BOOKING_ACCESS_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BookingPaymentNotAllowedException.class)
    public ResponseEntity<ApiError> handleBookingPaymentException(
            BookingPaymentNotAllowedException ex) {
        ApiError apiError = new ApiError(
                BOOKING_PAYMENT_NOT_ALLOWED_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(
                ENTITY_NOT_FOUND_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoAvailableUnitsException.class)
    public ResponseEntity<ApiError> handleNoAvailableUnitsException(NoAvailableUnitsException ex) {
        ApiError apiError = new ApiError(
                NO_AVAILABLE_UNIT_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidPaymentStatusException.class)
    public ResponseEntity<ApiError> handleInvalidPaymentStatusException(
            InvalidPaymentStatusException ex) {
        ApiError apiError = new ApiError(
                INVALID_PAYMENT_STATUS_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TelegramApiRequestException.class)
    public ResponseEntity<ApiError> handleTelegramApiRequestException(
            TelegramApiRequestException ex) {
        ApiError apiError = new ApiError(
                TELEGRAM_API_REQUEST_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(StripePaymentSessionException.class)
    public ResponseEntity<ApiError> handleStripePaymentSessionException(
            StripePaymentSessionException ex) {
        ApiError apiError = new ApiError(
                STRIPE_PAYMENT_SESSION_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(StripeSessionCancellationException.class)
    public ResponseEntity<ApiError> handleStripeSessionCancelException(
            StripeSessionCancellationException ex) {
        ApiError apiError = new ApiError(
                STRIPE_SESSION_CANCEL_EXCEPTION,
                ex.getMessage(),
                ZonedDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
