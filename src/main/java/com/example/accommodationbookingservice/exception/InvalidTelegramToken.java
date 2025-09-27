package com.example.accommodationbookingservice.exception;

public class InvalidTelegramToken extends RuntimeException {

    public InvalidTelegramToken(String message) {
        super(message);
    }
}
