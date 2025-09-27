package com.example.accommodationbookingservice.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
    @Email
    private String email;
    private String firstName;
    private String lastName;
}
