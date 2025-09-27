package com.example.accommodationbookingservice.exception;

public class NoAvailableUnitsException extends RuntimeException {
    public NoAvailableUnitsException(String message) {
        super(message);
    }
}
