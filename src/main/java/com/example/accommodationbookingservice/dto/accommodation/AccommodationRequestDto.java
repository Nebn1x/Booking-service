package com.example.accommodationbookingservice.dto.accommodation;

import com.example.accommodationbookingservice.dto.address.AddressRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccommodationRequestDto {
    @NotNull
    @Positive
    private Long typeId;
    @NotNull
    private AddressRequestDto addressDto;
    @NotBlank
    private String sizeType;
    @NotEmpty
    private Set<Long> amenityTypeIds;
    @NotNull
    @Positive
    private BigDecimal dailyRate;
    @Positive
    @NotNull
    private Integer availability;
}
