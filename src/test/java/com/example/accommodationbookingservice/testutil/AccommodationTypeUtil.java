package com.example.accommodationbookingservice.testutil;

import com.example.accommodationbookingservice.dto.accommodationtypedto.AccommodationTypeDto;
import com.example.accommodationbookingservice.entity.accommodation.AccommodationType;

public class AccommodationTypeUtil {

    public static AccommodationType getAccommodationType() {
        AccommodationType accommodationType = new AccommodationType();
        accommodationType.setId(1L);
        accommodationType.setName(AccommodationType.AccommodationTypeName.APARTMENT);
        return accommodationType;
    }

    public static AccommodationTypeDto getAccommodationTypeDto() {
        AccommodationTypeDto accommodationTypeDto = new AccommodationTypeDto();
        accommodationTypeDto.setId(1L);
        accommodationTypeDto.setName("APARTMENT");

        return accommodationTypeDto;
    }
}
