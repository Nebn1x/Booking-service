package com.example.accommodationbookingservice.controller;

import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import com.example.accommodationbookingservice.dto.user.UserUpdatePasswordRequestDto;
import com.example.accommodationbookingservice.dto.user.UserUpdateRequestDto;
import com.example.accommodationbookingservice.dto.user.UserUpdateRoleRequestDto;
import com.example.accommodationbookingservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Management",
        description = "Endpoints for managing user registration, "
                + "authentication, and profile information")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    public static final String PASSWORD_UPDATED_SUCCESSFULLY = "Password updated successfully";
    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(
            summary = "Retrieve authenticated user details",
            description = "Returns the profile information of the currently authenticated user, "
                    + "including personal data and assigned roles."
    )
    public UserResponseDto getInfo(Authentication authentication) {
        return userService.getInfo(authentication.getName());
    }

    @PatchMapping("/me")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(
            summary = "Update current user profile",
            description = "Allows the authenticated user to update their profile information, "
                    + "including email, first name, and last name. "
    )
    public UserResponseDto update(
            Authentication authentication,
            @RequestBody @Valid UserUpdateRequestDto requestDto) {
        return userService.update(authentication.getName(), requestDto);
    }

    @PutMapping("/me/password")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Update user password",
            description = "Allows an authenticated user with the CUSTOMER role "
                    + "to update their account password. The request must include "
                    + "the current password, a new password, and a confirmation of the "
                    + "new password."
    )
    public ResponseEntity<String> updatePassword(
            Authentication authentication,
            @RequestBody @Valid UserUpdatePasswordRequestDto requestDto) {
        userService.updatePassword(authentication.getName(), requestDto);
        return ResponseEntity.ok(PASSWORD_UPDATED_SUCCESSFULLY);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Update user role by ID",
            description = "Allows an ADMIN to update the role of an existing user by their ID. "
                    + "Requires a valid role value in the request body."
    )
    public UserResponseDto updateRoles(@PathVariable Long id,
                                       @RequestBody @Valid UserUpdateRoleRequestDto requestDto) {
        return userService.updateRoles(id, requestDto);
    }
}

