package com.example.accommodationbookingservice.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank
        @Size(min = 8)
        @Email String email,
        @NotBlank
        @Size(min = 8, max = 30)
        String password) {
}
