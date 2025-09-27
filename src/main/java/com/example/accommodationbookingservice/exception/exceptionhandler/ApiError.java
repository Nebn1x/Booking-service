package com.example.accommodationbookingservice.exception.exceptionhandler;

import java.time.ZonedDateTime;

public record ApiError(String errorType, String message, ZonedDateTime dateTime) {}
