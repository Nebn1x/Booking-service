package com.example.accommodationbookingservice.testutil;

import com.example.accommodationbookingservice.entity.accommodation.AmenityType;

public class AmenityTypeUtil {

    public static AmenityType getAmenityType() {
        AmenityType amenityType = new AmenityType();
        amenityType.setId(1L);
        amenityType.setName(AmenityType.AmenityTypeName.DISHWASHER);
        amenityType.setName(AmenityType.AmenityTypeName.SAFE);
        return amenityType;
    }

}
