package com.example.accommodationbookingservice.controller;

import com.example.accommodationbookingservice.dto.bookingdto.BookingDto;
import com.example.accommodationbookingservice.dto.bookingdto.BookingRequestDto;
import com.example.accommodationbookingservice.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Booking Controller",
        description = "Provides endpoints for managing accommodation bookings, including creation, "
                + "retrieval, updating, and cancellation. Supports both user and manager access.")
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Create a new booking",
            description = "Allows a user with the CUSTOMER role to create a new "
                    + "booking for an accommodation.")
    public BookingDto save(@RequestBody @Valid BookingRequestDto requestDto,
                           Authentication authentication) {
        return bookingService.save(requestDto, authentication.getName());
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Retrieve current user's bookings",
            description = "Returns a paginated list of all bookings "
                    + "associated with the currently authenticated user")
    public List<BookingDto> getUserBookings(Authentication authentication, Pageable pageable) {
        return bookingService.findByUserEmail(authentication.getName(), pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Retrieve a specific booking by ID",
            description = "Returns the booking details for the given ID if it belongs "
                    + "to the authenticated user. Only accessible to users with "
                    + "CUSTOMER authority.")
    public BookingDto getBookingsById(@PathVariable Long id, Authentication authentication) {
        return bookingService.getById(id, authentication.getName());
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Retrieve all bookings",
            description = "Returns a paginated list of all bookings in the system. "
                    + "Accessible only to users with ADMIN authority."
    )
    public List<BookingDto> getAllBookings(Pageable pageable) {
        return bookingService.findAllBookings(pageable);
    }

    @GetMapping("/{id}/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<BookingDto> getBookingByUser(@PathVariable Long id) {
        return bookingService.getBookingByUserId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Update a booking",
            description = "Allows a customer to update the check-in and check-out dates "
                    + "of their booking. Accessible only by the user who owns the booking.")
    public BookingDto update(@PathVariable Long id,
                             @RequestBody @Valid BookingRequestDto requestDto,
                             Authentication authentication) {
        return bookingService.updateInfo(id, requestDto, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Cancel a booking",
            description = "Allows a customer to cancel their booking if it has a 'PENDING' status. "
                    + "Only the owner of the booking can perform this operation.")
    public BookingDto cancelBookingById(@PathVariable Long id,
                                        Authentication authentication) {
        return bookingService.cancel(id, authentication.getName());
    }
}
