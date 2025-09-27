package com.example.accommodationbookingservice.service.impl;

import com.example.accommodationbookingservice.dto.bookingdto.BookingDto;
import com.example.accommodationbookingservice.dto.bookingdto.BookingRequestDto;
import com.example.accommodationbookingservice.entity.accommodation.Accommodation;
import com.example.accommodationbookingservice.entity.booking.Booking;
import com.example.accommodationbookingservice.entity.booking.BookingStatus;
import com.example.accommodationbookingservice.entity.user.User;
import com.example.accommodationbookingservice.exception.EntityNotFoundException;
import com.example.accommodationbookingservice.exception.InvalidDateException;
import com.example.accommodationbookingservice.exception.InvalidReservationStatusException;
import com.example.accommodationbookingservice.exception.NoAvailableUnitsException;
import com.example.accommodationbookingservice.exception.PermissionException;
import com.example.accommodationbookingservice.mapper.BookingMapper;
import com.example.accommodationbookingservice.repository.AccommodationRepository;
import com.example.accommodationbookingservice.repository.BookingRepository;
import com.example.accommodationbookingservice.repository.BookingStatusRepository;
import com.example.accommodationbookingservice.repository.UserRepository;
import com.example.accommodationbookingservice.service.BookingService;
import com.example.accommodationbookingservice.telegram.BookingNotificationBot;
import com.example.accommodationbookingservice.telegram.TelegramNotificationBuilder;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final Long NO_AVAILABLE_UNITS = 0L;
    private static final BookingStatus.BookingStatusName STATUS_PENDING =
            BookingStatus.BookingStatusName.PENDING;
    private static final BookingStatus.BookingStatusName STATUS_CANCELED =
            BookingStatus.BookingStatusName.CANCELED;
    private static final String CANNOT_ACCESS_BOOKING = " cannot access booking ";
    private static final String BOOKING_NOT_FOUND_WITH_ID = "Booking not found with id: ";
    private static final String CAN_T_FIND_BOOKING_STATUS = "Can't find booking status";
    private static final String USER_NOT_FOUND = "User not found: ";
    private static final String NO_AVAILABLE_UNITS_FOR_ACCOMMODATION_ID =
            "No available units for accommodation ID: ";
    private static final String NO_AVAILABLE_UNITS_FOR_THE_SELECTED_DATES =
            "No available units for the selected dates";
    private static final String ACCOMMODATION_NOT_FOUND_WITH_ID =
            "Accommodation not found with ID: ";
    private static final String BOOKING_STATUS_MISMATCH_EXPECTED =
            "Booking status mismatch: expected ";
    private static final String BUT_FOUND = " but found ";
    private static final String CHECK_IN_DATE =
            "Check-in date must not be in the past and check-out "
            + "date must be after check-in date.";

    private final UserRepository userRepo;
    private final AccommodationRepository accommodationRepo;
    private final BookingRepository bookingRepo;
    private final BookingStatusRepository statusRepo;
    private final BookingMapper bookingMapper;
    private final BookingNotificationBot telegramBot;

    @Override
    public BookingDto save(BookingRequestDto request, String userEmail) {
        validateBookingDates(request.getCheckInDate(), request.getCheckOutDate());
        Booking booking = bookingMapper.toModelWithoutStatusAndUser(request);
        Accommodation accommodation = fetchAccommodationIfAvailable(
                request.getAccommodationId(),
                request.getCheckInDate(),
                request.getCheckOutDate());

        booking.setAccommodation(accommodation);
        User user = fetchUser(userEmail);
        booking.setUser(user);
        booking.setStatus(fetchStatus(STATUS_PENDING));
        Booking saved = bookingRepo.save(booking);
        telegramBot.sendMessage(userEmail, TelegramNotificationBuilder.bookingCreated(saved));
        return bookingMapper.toDto(saved);
    }

    @Override
    public BookingDto updateInfo(Long bookingId, BookingRequestDto request, String userEmail) {
        Booking booking = getBookingForUserOrFail(bookingId, userEmail);
        validateBookingDates(request.getCheckInDate(), request.getCheckOutDate());
        ensureStatus(booking);
        verifyAvailability(
                request.getAccommodationId(),
                request.getCheckInDate(),
                request.getCheckOutDate()
        );
        bookingMapper.setUpdateInfoToBooking(booking, request);
        bookingRepo.save(booking);
        telegramBot.sendMessage(userEmail, TelegramNotificationBuilder.bookingUpdated(booking));
        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDto getById(Long id, String email) {
        return bookingMapper.toDto(getBookingForUserOrFail(id, email));
    }

    @Override
    public List<BookingDto> getBookingByUserId(Long userId) {
        List<Booking> byUserId = bookingRepo.findByUserId(userId);
        return bookingMapper.toDtos(byUserId);
    }

    @Override
    public List<BookingDto> findAllBookings(Pageable pageable) {
        return bookingRepo.findAll(pageable).stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    public List<BookingDto> findByUserEmail(String email, Pageable pageable) {
        return bookingRepo.findAllByUser_Email(email, pageable).stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    public BookingDto cancel(Long bookingId, String userEmail) {
        Booking booking = getBookingForUserOrFail(bookingId, userEmail);
        ensureStatus(booking);
        booking.setStatus(fetchStatus(STATUS_CANCELED));
        bookingRepo.save(booking);
        telegramBot.sendMessage(userEmail, TelegramNotificationBuilder.bookingCancelled(booking));
        return bookingMapper.toDto(booking);
    }

    private void validateBookingDates(LocalDate checkIn, LocalDate checkOut) {
        LocalDate today = LocalDate.now();

        if (checkIn.isBefore(today) || !checkOut.isAfter(checkIn)) {
            throw new InvalidDateException(CHECK_IN_DATE);
        }
    }

    private void ensureStatus(Booking booking) {
        if (!booking.getStatus().getName().equals(BookingServiceImpl.STATUS_PENDING)) {
            throw new InvalidReservationStatusException(BOOKING_STATUS_MISMATCH_EXPECTED
                    + BookingServiceImpl.STATUS_PENDING
                    + BUT_FOUND
                    + booking.getStatus().getName());
        }
    }

    private Accommodation fetchAccommodationIfAvailable(Long id, LocalDate in, LocalDate out) {
        Accommodation accommodation = accommodationRepo.findById(id)
                .orElseThrow(()
                        -> new EntityNotFoundException(ACCOMMODATION_NOT_FOUND_WITH_ID + id));

        Long active = bookingRepo.isDatesAvailableForAccommodation(id, in, out);
        if (active >= accommodation.getAvailability()) {
            throw new NoAvailableUnitsException(NO_AVAILABLE_UNITS_FOR_THE_SELECTED_DATES);
        }

        return accommodation;
    }

    private void verifyAvailability(Long accommodationId, LocalDate checkInDate,
                                    LocalDate checkOutDate) {
        Long active = bookingRepo.isDatesAvailableForAccommodation(
                accommodationId,
                checkInDate,
                checkOutDate
        );

        if (active > BookingServiceImpl.NO_AVAILABLE_UNITS) {
            throw new NoAvailableUnitsException(NO_AVAILABLE_UNITS_FOR_ACCOMMODATION_ID
                    + accommodationId);
        }
    }

    private User fetchUser(String email) {
        return userRepo.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + email));
    }

    private BookingStatus fetchStatus(BookingStatus.BookingStatusName name) {
        return statusRepo.findByName(name).orElseThrow(
                () -> new EntityNotFoundException(CAN_T_FIND_BOOKING_STATUS));
    }

    private Booking getBookingForUserOrFail(Long id, String email) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOKING_NOT_FOUND_WITH_ID + id));

        if (!booking.getUser().getEmail().equals(email)) {
            throw new PermissionException(email + CANNOT_ACCESS_BOOKING + id);
        }
        return booking;
    }
}
