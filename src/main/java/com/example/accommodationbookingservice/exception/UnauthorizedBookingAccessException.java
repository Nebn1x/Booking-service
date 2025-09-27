package com.example.accommodationbookingservice.exception;

public class UnauthorizedBookingAccessException extends RuntimeException {
    public UnauthorizedBookingAccessException(String message) {
        super(message);
    }
}
