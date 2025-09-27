package com.example.accommodationbookingservice.exception;

public class StripePaymentSessionException extends RuntimeException {
    public StripePaymentSessionException(String message) {
        super(message);
    }
}
