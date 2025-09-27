package com.example.accommodationbookingservice.testutil;

import com.example.accommodationbookingservice.dto.bookingstatusdto.BookingStatusDto;
import com.example.accommodationbookingservice.entity.booking.BookingStatus;

public class BookingStatusUtil {

    public static BookingStatus getBookingStatusPending() {
        BookingStatus bookingStatus = new BookingStatus();
        BookingStatus.BookingStatusName bookingStatusName = BookingStatus.BookingStatusName.PENDING;
        bookingStatus.setId(1L);
        bookingStatus.setName(bookingStatusName);
        return bookingStatus;
    }

    public static BookingStatus getBookingStatusCanceled() {
        BookingStatus bookingStatus = new BookingStatus();
        BookingStatus.BookingStatusName bookingStatusName =
                BookingStatus.BookingStatusName.CANCELED;
        bookingStatus.setId(1L);
        bookingStatus.setName(bookingStatusName);
        return bookingStatus;
    }

    public static BookingStatusDto getBookingStatusCanceledDto() {
        BookingStatusDto bookingStatusDto = new BookingStatusDto();
        bookingStatusDto.setId(1L);
        bookingStatusDto.setName("CANCELED");
        return bookingStatusDto;
    }

    public static BookingStatusDto getBookingStatusCanceledDto2() {
        BookingStatusDto bookingStatusDto = new BookingStatusDto();
        bookingStatusDto.setId(3L);
        bookingStatusDto.setName("CANCELED");
        return bookingStatusDto;
    }

    public static BookingStatusDto getBookingStatusDto() {
        BookingStatusDto bookingStatusDto = new BookingStatusDto();
        bookingStatusDto.setId(1L);
        bookingStatusDto.setName("PENDING");
        return bookingStatusDto;
    }

    public static BookingStatusDto getBookingStatusDtoExpired() {
        BookingStatusDto bookingStatusDto = new BookingStatusDto();
        bookingStatusDto.setId(4L);
        bookingStatusDto.setName("EXPIRED");
        return bookingStatusDto;
    }

}
