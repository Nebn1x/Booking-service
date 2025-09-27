package com.example.accommodationbookingservice.dto.accommodation;

import com.example.accommodationbookingservice.dto.accommodationtypedto.AccommodationTypeDto;
import com.example.accommodationbookingservice.dto.address.AddressDto;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccommodationDto {
    private Long id;
    private AccommodationTypeDto type;
    private AddressDto address;
    private String sizeType;
    private Set<AmenityTypeDto> amenities;
    private BigDecimal dailyRate;
}
