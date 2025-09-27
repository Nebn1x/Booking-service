package com.example.accommodationbookingservice.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UserUpdatePasswordRequestDto {
    @Length(min = 8, max = 30)
    @NotBlank
    private String oldPassword;
    @Length(min = 8, max = 30)
    @NotBlank
    private String newPassword;
    @Length(min = 8, max = 30)
    @NotBlank
    private String confirmPassword;
}
