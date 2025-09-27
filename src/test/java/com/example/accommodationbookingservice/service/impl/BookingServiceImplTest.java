package com.example.accommodationbookingservice.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.accommodationbookingservice.dto.bookingdto.BookingDto;
import com.example.accommodationbookingservice.dto.bookingdto.BookingRequestDto;
import com.example.accommodationbookingservice.entity.accommodation.Accommodation;
import com.example.accommodationbookingservice.entity.booking.Booking;
import com.example.accommodationbookingservice.entity.booking.BookingStatus;
import com.example.accommodationbookingservice.entity.user.User;
import com.example.accommodationbookingservice.exception.EntityNotFoundException;
import com.example.accommodationbookingservice.exception.InvalidDateException;
import com.example.accommodationbookingservice.mapper.BookingMapper;
import com.example.accommodationbookingservice.repository.AccommodationRepository;
import com.example.accommodationbookingservice.repository.BookingRepository;
import com.example.accommodationbookingservice.repository.BookingStatusRepository;
import com.example.accommodationbookingservice.repository.UserRepository;
import com.example.accommodationbookingservice.telegram.BookingNotificationBot;
import com.example.accommodationbookingservice.testutil.AccommodationUtil;
import com.example.accommodationbookingservice.testutil.BookingStatusUtil;
import com.example.accommodationbookingservice.testutil.BookingUtil;
import com.example.accommodationbookingservice.testutil.UserUtil;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private UserRepository userRepo;
    @Mock
    private AccommodationRepository accommodationRepo;
    @Mock
    private BookingRepository bookingRepo;
    @Mock
    private BookingStatusRepository statusRepo;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private BookingNotificationBot telegramBot;
    @InjectMocks
    private BookingServiceImpl bookingService;

    private Accommodation accommodation;
    private Booking pendingBooking;
    private Booking canceledBooking;
    private BookingRequestDto bookingRequestDto;
    private BookingDto bookingDto;
    private BookingDto canceledBookingDto;
    private BookingStatus bookingStatusPending;
    private BookingStatus bookingStatusCanceled;
    private List<Booking> getListBooking;
    private List<BookingDto> getListBookingDto;
    private User user;

    @BeforeEach
    void setUp() {
        accommodation = AccommodationUtil.getAccommodation();
        pendingBooking = BookingUtil.getPendingBooking();
        canceledBooking = BookingUtil.getCanceledBooking();
        bookingDto = BookingUtil.getBookingDto();
        canceledBookingDto = BookingUtil.getBookingDtoCanceledStatus();
        bookingRequestDto = BookingUtil.getBookingRequestDto();
        bookingStatusPending = BookingStatusUtil.getBookingStatusPending();
        bookingStatusCanceled = BookingStatusUtil.getBookingStatusCanceled();
        user = UserUtil.getUser();
        getListBooking = BookingUtil.getBookingList();
        getListBookingDto = BookingUtil.getBookingListDto();
    }

    @Test
    @DisplayName("Should return BookingDto when saving booking "
            + "with valid data and available accommodation")
    void save_ValidBookingWithValidData_ShouldReturnBookingDto() {
        String email = "test@gmail.com";
        BookingDto expectedBooking = bookingDto;
        when(bookingMapper.toModelWithoutStatusAndUser(bookingRequestDto))
                .thenReturn(pendingBooking);
        when(accommodationRepo.findById(1L)).thenReturn(Optional.of(accommodation));
        when(bookingRepo.isDatesAvailableForAccommodation(1L,
                LocalDate.of(2025, Month.SEPTEMBER, 11),
                LocalDate.of(2025, Month.SEPTEMBER, 15))).thenReturn(4L);
        when(userRepo.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(statusRepo.findByName(BookingStatus.BookingStatusName.PENDING))
                .thenReturn(Optional.of(bookingStatusPending));
        when(bookingRepo.save(pendingBooking)).thenReturn(pendingBooking);
        when(bookingMapper.toDto(pendingBooking)).thenReturn(bookingDto);
        BookingDto actualBooking = bookingService.save(bookingRequestDto, email);
        assertThat(actualBooking).usingRecursiveComparison().isEqualTo(expectedBooking);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when saving "
            + "booking with non-existing accommodation")
    void save_BookingWithNoExistingAccommodation_ShouldReturnException() {
        String email = "test@gmail.com";
        long id = 1L;
        EntityNotFoundException actualMessage = assertThrows(EntityNotFoundException.class,
                () -> bookingService.save(bookingRequestDto, email));
        String expectedMessage = "Accommodation not found with ID: " + id;
        assertEquals(expectedMessage, actualMessage.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidDateException when check-out "
            + "date is before check-in date")
    void save_BookingWithIncorrectDate_ShouldReturnException() {
        String email = "test@gmail.com";
        LocalDate in = LocalDate.of(2025, Month.SEPTEMBER, 11);
        LocalDate out = LocalDate.of(2024, Month.SEPTEMBER, 11);
        bookingRequestDto.setCheckInDate(in);
        bookingRequestDto.setCheckOutDate(out);
        InvalidDateException actualException = assertThrows(InvalidDateException.class,
                () -> bookingService.save(bookingRequestDto, email));
        String expectedMessage = "Check-in date must not be in the past "
                + "and check-out date must be after check-in date.";
        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should update booking dates successfully when valid data is provided")
    void updateInfo_BookingRequestWithValidData_ShouldReturnBookingDto() {
        String email = "test@gmail.com";
        LocalDate in = LocalDate.of(2025, Month.SEPTEMBER, 11);
        LocalDate out = LocalDate.of(2025, Month.SEPTEMBER, 15);
        when(bookingRepo.findById(1L)).thenReturn(Optional.of(pendingBooking));
        when(bookingRepo.isDatesAvailableForAccommodation(1L, in, out))
                .thenReturn(0L);
        when(bookingMapper.toDto(pendingBooking)).thenReturn(bookingDto);
        BookingDto acutalBookingDto = bookingService.updateInfo(1L, bookingRequestDto, email);
        assertThat(acutalBookingDto).usingRecursiveComparison().isEqualTo(bookingDto);
    }

    @Test
    @DisplayName("Should return BookingDto when booking with given ID exists for the user")
    void getById_ExistingBookDto_ShouldReturnBookDto() {
        String email = "test@gmail.com";
        when(bookingMapper.toDto(pendingBooking)).thenReturn(bookingDto);
        when(bookingRepo.findById(1L)).thenReturn(Optional.of(pendingBooking));
        BookingDto actualBooking = bookingService.getById(1L, email);
        assertEquals(bookingDto, actualBooking);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when booking "
            + "with given ID does not exist")
    void getById_NoExistingBookDto_ShouldReturnException() {
        String email = "test@gmail.com";
        Long id = 99L;
        EntityNotFoundException actualException = assertThrows(EntityNotFoundException.class,
                () -> bookingService.getById(id, email));
        String actual = "Booking not found with id: " + id;
        Assertions.assertEquals(actual, actualException.getMessage());
    }

    @Test
    @DisplayName("Should return list of BookingDto when bookings "
            + "exist for given user ID")
    void getBookingByUserId_ValidId_ShouldReturnListBookDto() {
        when(bookingRepo.findByUserId(1L)).thenReturn(getListBooking);
        when(bookingMapper.toDtos(anyList())).thenReturn(getListBookingDto);
        List<BookingDto> actualBookingByUserId = bookingService.getBookingByUserId(1L);
        assertThat(actualBookingByUserId).usingRecursiveComparison().isEqualTo(getListBookingDto);
    }

    @Test
    @DisplayName("Should return paginated list of BookingDto when bookings exist")
    void findAllBookings_ExistingBookings_ShouldReturnListBookingDto() {
        List<BookingDto> expectedBookings = getListBookingDto;
        Pageable pageableList = Pageable.ofSize(1);
        Page<Booking> page = new PageImpl<>(getListBooking);

        when(bookingMapper.toDto(any(Booking.class))).thenReturn(bookingDto);
        when(bookingRepo.findAll(pageableList)).thenReturn(page);
        List<BookingDto> actualBookings = bookingService.findAllBookings(pageableList);
        assertThat(actualBookings).usingRecursiveComparison().isEqualTo(expectedBookings);
    }

    @Test
    @DisplayName("Should return empty list when no bookings are found")
    void findAllBookings_NoExistingBookings_ShouldReturnEmptyList() {
        Pageable pageableList = Pageable.ofSize(1);
        Page<Booking> page = new PageImpl<>(List.of());
        when(bookingRepo.findAll(pageableList)).thenReturn(page);
        List<BookingDto> actualBookings = bookingService.findAllBookings(pageableList);

        assertTrue(actualBookings.isEmpty());
    }

    @Test
    @DisplayName("Should return list of BookingDto when bookings "
            + "exist for specified user email")
    void findByUserEmail_ExistingBookings_ShouldReturnListBookingDto() {
        String email = "test@gmail.com";
        Pageable pageable = Pageable.ofSize(1);
        List<BookingDto> expectedList = getListBookingDto;

        when(bookingRepo.findAllByUser_Email(eq(email),any(Pageable.class)))
                .thenReturn(getListBooking);
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(bookingDto);

        List<BookingDto> actualBookingsByUserEmail = bookingService
                .findByUserEmail(email, pageable);

        assertThat(actualBookingsByUserEmail).usingRecursiveComparison().isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Should cancel booking, update status")
    void cancel_ValidBooking_ShouldUpdateStatusSendTelegramAndReturnDto() {
        Long bookingId = 1L;
        String userEmail = "test@gmail.com";
        BookingDto expectedDto = canceledBookingDto;
        BookingStatus.BookingStatusName bookingStatusName = BookingStatus
                .BookingStatusName.CANCELED;

        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(pendingBooking));
        when(bookingRepo.save(any(Booking.class))).thenReturn(canceledBooking);
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(canceledBookingDto);
        when(statusRepo.findByName(bookingStatusName))
                .thenReturn(Optional.of(bookingStatusCanceled));

        BookingDto actualBookingDto = bookingService.cancel(bookingId, userEmail);

        assertThat(actualBookingDto).usingRecursiveComparison().isEqualTo(expectedDto);
    }
}

