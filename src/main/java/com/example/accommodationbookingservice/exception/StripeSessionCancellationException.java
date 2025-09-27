package com.example.accommodationbookingservice.exception;

public class StripeSessionCancellationException extends RuntimeException {
    public StripeSessionCancellationException(String message) {
        super(message);
    }
}
