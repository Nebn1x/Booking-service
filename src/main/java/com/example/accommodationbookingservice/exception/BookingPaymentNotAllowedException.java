package com.example.accommodationbookingservice.exception;

public class BookingPaymentNotAllowedException extends RuntimeException {
    public BookingPaymentNotAllowedException(String message) {
        super(message);
    }
}
