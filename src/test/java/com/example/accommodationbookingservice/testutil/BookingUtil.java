package com.example.accommodationbookingservice.testutil;

import com.example.accommodationbookingservice.dto.bookingdto.BookingDto;
import com.example.accommodationbookingservice.dto.bookingdto.BookingRequestDto;
import com.example.accommodationbookingservice.entity.booking.Booking;
import com.example.accommodationbookingservice.entity.booking.BookingStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class BookingUtil {

    public static Booking getPendingBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setAccommodation(AccommodationUtil.getAccommodation());
        booking.setUser(UserUtil.getUser());
        booking.setDeleted(false);
        booking.setCheckInDate(LocalDate.of(2025, Month.SEPTEMBER, 11));
        booking.setCheckOutDate(LocalDate.of(2025, Month.SEPTEMBER, 15));
        booking.setBookingCreatedAt(LocalDateTime.of(2025, Month.JUNE, 11,2,10));
        booking.setStatus(BookingStatusUtil.getBookingStatusPending());
        return booking;
    }

    public static Booking getCanceledBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setAccommodation(AccommodationUtil.getAccommodation());
        booking.setUser(UserUtil.getUser());
        booking.setDeleted(false);
        booking.setCheckInDate(LocalDate.of(2025, Month.SEPTEMBER, 11));
        booking.setCheckOutDate(LocalDate.of(2025, Month.SEPTEMBER, 15));
        booking.setBookingCreatedAt(LocalDateTime.of(2025, Month.JUNE, 11,2,10));
        booking.setStatus(BookingStatusUtil.getBookingStatusCanceled());
        return booking;
    }

    public static BookingDto getBookingDto() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setCreateAt(LocalDateTime.of(2025, Month.JUNE, 11,2,10));
        bookingDto.setCheckInDate(LocalDate.of(2025, Month.SEPTEMBER, 11));
        bookingDto.setCheckOutDate(LocalDate.of(2025, Month.SEPTEMBER, 15));
        bookingDto.setAccommodation(AccommodationUtil.getAccommodationDto());
        bookingDto.setStatus(BookingStatusUtil.getBookingStatusDto());
        bookingDto.setUser(UserUtil.getUserResponseDto());
        return bookingDto;
    }

    public static BookingDto getBookingDto2() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(3L);
        bookingDto.setCheckInDate(LocalDate.of(2034, Month.NOVEMBER, 10));
        bookingDto.setCheckOutDate(LocalDate.of(2034, Month.NOVEMBER, 20));
        bookingDto.setAccommodation(AccommodationUtil.getExpectedAccommodationDto2());
        bookingDto.setStatus(BookingStatusUtil.getBookingStatusDto());
        bookingDto.setUser(UserUtil.getUserResponseDto2());
        return bookingDto;
    }

    public static BookingRequestDto getBookingRequestDto() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setAccommodationId(1L);
        bookingRequestDto.setCheckInDate(LocalDate.of(2025, Month.SEPTEMBER, 11));
        bookingRequestDto.setCheckOutDate(LocalDate.of(2025, Month.SEPTEMBER, 15));
        return bookingRequestDto;
    }

    public static BookingRequestDto getBookingRequestDto2() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setAccommodationId(1L);
        bookingRequestDto.setCheckInDate(LocalDate.of(2034, Month.NOVEMBER, 10));
        bookingRequestDto.setCheckOutDate(LocalDate.of(2034, Month.NOVEMBER, 20));
        return bookingRequestDto;
    }

    public static BookingRequestDto getBookingRequestDto3() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setAccommodationId(1L);
        bookingRequestDto.setCheckInDate(LocalDate.of(2035, Month.NOVEMBER, 10));
        bookingRequestDto.setCheckOutDate(LocalDate.of(2035, Month.NOVEMBER, 20));
        return bookingRequestDto;
    }

    public static Booking getExpectedBooking() {
        Booking booking = new Booking();
        booking.setCheckInDate(LocalDate.of(2034, Month.NOVEMBER, 10));
        booking.setCheckOutDate(LocalDate.of(2034, Month.NOVEMBER, 20));
        booking.setBookingCreatedAt(LocalDateTime.of(2034, Month.NOVEMBER, 10,
                12, 0));
        BookingStatus bookingStatus = new BookingStatus();
        BookingStatus.BookingStatusName name =
                BookingStatus.BookingStatusName.PENDING;
        bookingStatus.setName(name);
        booking.setStatus(bookingStatus);
        return booking;
    }

    public static BookingDto getExpectedBookingDto() {
        BookingDto booking = new BookingDto();
        booking.setId(1L);
        booking.setCheckInDate(LocalDate.of(2034, Month.NOVEMBER, 10));
        booking.setCheckOutDate(LocalDate.of(2034, Month.NOVEMBER, 20));
        booking.setAccommodation(AccommodationUtil.getExpectedAccommodationDto2());
        booking.setUser(UserUtil.getUserResponseDto2());
        booking.setStatus(BookingStatusUtil.getBookingStatusDto());

        return booking;
    }

    public static BookingDto getExpectedBookingDto2() {
        BookingDto booking = new BookingDto();
        booking.setId(2L);
        booking.setCheckInDate(LocalDate.of(2034, Month.NOVEMBER, 20));
        booking.setCheckOutDate(LocalDate.of(2034, Month.NOVEMBER, 30));
        booking.setAccommodation(AccommodationUtil.getExpectedAccommodationDto2());
        booking.setUser(UserUtil.getUserResponseDto2());
        booking.setStatus(BookingStatusUtil.getBookingStatusDtoExpired());

        return booking;
    }

    public static BookingDto getExpectedBookingDto3() {
        BookingDto booking = new BookingDto();
        booking.setId(1L);
        booking.setCheckInDate(LocalDate.of(2035, Month.NOVEMBER, 10));
        booking.setCheckOutDate(LocalDate.of(2035, Month.NOVEMBER, 20));
        booking.setAccommodation(AccommodationUtil.getExpectedAccommodationDto2());
        booking.setUser(UserUtil.getUserResponseDto2());
        booking.setStatus(BookingStatusUtil.getBookingStatusDto());

        return booking;
    }

    public static BookingDto getExpectedBookingDto4() {
        BookingDto booking = new BookingDto();
        booking.setId(1L);
        booking.setCheckInDate(LocalDate.of(2034, Month.NOVEMBER, 10));
        booking.setCheckOutDate(LocalDate.of(2034, Month.NOVEMBER, 20));
        booking.setAccommodation(AccommodationUtil.getExpectedAccommodationDto2());
        booking.setUser(UserUtil.getUserResponseDto2());
        booking.setStatus(BookingStatusUtil.getBookingStatusCanceledDto2());

        return booking;
    }

    public static BookingDto getBookingDtoCanceledStatus() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setCreateAt(LocalDateTime.of(2025, Month.JUNE, 11,2,10));
        bookingDto.setCheckInDate(LocalDate.of(2025, Month.SEPTEMBER, 11));
        bookingDto.setCheckOutDate(LocalDate.of(2025, Month.SEPTEMBER, 15));
        bookingDto.setAccommodation(AccommodationUtil.getAccommodationDto());
        bookingDto.setStatus(BookingStatusUtil.getBookingStatusCanceledDto());
        bookingDto.setUser(UserUtil.getUserResponseDto());
        return bookingDto;
    }

    public static List<Booking> getBookingList() {
        return List.of(getPendingBooking());
    }

    public static List<BookingDto> getBookingListDto() {
        return List.of(getBookingDto());
    }

}
