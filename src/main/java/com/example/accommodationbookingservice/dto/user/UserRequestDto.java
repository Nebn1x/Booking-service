package com.example.accommodationbookingservice.dto.user;

import com.example.accommodationbookingservice.validation.FieldMatch;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@FieldMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match!"
)
@Getter
@Setter
public class UserRequestDto {
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;
    @Length(min = 8, max = 30)
    @NotBlank
    private String password;
    @Length(min = 8, max = 30)
    @NotBlank
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
