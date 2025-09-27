package com.example.accommodationbookingservice.exception;

public class PaymentNotConfirmedException extends RuntimeException {
    public PaymentNotConfirmedException(String message) {
        super(message);
    }
}
