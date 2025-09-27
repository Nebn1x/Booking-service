package com.example.accommodationbookingservice.testutil;

import com.example.accommodationbookingservice.dto.accommodation.AccommodationDto;
import com.example.accommodationbookingservice.dto.accommodation.AccommodationRequestDto;
import com.example.accommodationbookingservice.dto.accommodation.AmenityTypeDto;
import com.example.accommodationbookingservice.dto.accommodationtypedto.AccommodationTypeDto;
import com.example.accommodationbookingservice.dto.address.AddressDto;
import com.example.accommodationbookingservice.dto.address.AddressRequestDto;
import com.example.accommodationbookingservice.entity.accommodation.Accommodation;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

public class AccommodationUtil {

    public static AccommodationRequestDto createSampleAccommodationRequestDto() {
        AccommodationRequestDto dto = new AccommodationRequestDto();
        dto.setTypeId(1L);
        AddressRequestDto addressDto = new AddressRequestDto();
        addressDto.setCountry("Ukraine");
        addressDto.setState("Lviv Region");
        addressDto.setCity("Lviv");
        addressDto.setStreet("Shevchenko");
        addressDto.setHouseNumber("33");
        addressDto.setApartmentNumber("3");
        addressDto.setFloor("3");
        addressDto.setZipCode("5353");
        dto.setAddressDto(addressDto);
        dto.setSizeType("Studio");

        Set<Long> amenityTypeIds = new HashSet<>();
        amenityTypeIds.add(1L);
        amenityTypeIds.add(2L);
        dto.setAmenityTypeIds(amenityTypeIds);

        dto.setDailyRate(BigDecimal.valueOf(250.00).setScale(2, RoundingMode.HALF_UP));
        dto.setAvailability(5);

        return dto;
    }

    public static AccommodationRequestDto getAccommodationUpdateRequestDto() {
        AccommodationRequestDto dto = new AccommodationRequestDto();
        dto.setTypeId(1L);
        AddressRequestDto addressDto = new AddressRequestDto();
        addressDto.setCountry("Ukraine");
        addressDto.setState("Kyiv Region");
        addressDto.setCity("Kyiv");
        addressDto.setStreet("Shevchenko");
        addressDto.setHouseNumber("22");
        addressDto.setApartmentNumber("4");
        addressDto.setFloor("1");
        addressDto.setZipCode("5353");
        dto.setAddressDto(addressDto);
        dto.setSizeType("Studio");

        Set<Long> amenityTypeIds = new HashSet<>();
        amenityTypeIds.add(1L);
        amenityTypeIds.add(2L);
        dto.setAmenityTypeIds(amenityTypeIds);

        dto.setDailyRate(BigDecimal.valueOf(250.00).setScale(2, RoundingMode.HALF_UP));
        dto.setAvailability(5);

        return dto;
    }

    public static AccommodationDto getAccommodationDtoAfterUpdate() {
        AccommodationDto accommodationDto = new AccommodationDto();
        accommodationDto.setId(1L);
        AddressDto addressDto = new AddressDto();
        addressDto.setCountry("Ukraine");
        addressDto.setState("Kyiv Region");
        addressDto.setCity("Kyiv");
        addressDto.setStreet("Shevchenko");
        addressDto.setHouseNumber("22");
        addressDto.setApartmentNumber("4");
        addressDto.setFloor("1");
        addressDto.setZipCode("5353");
        accommodationDto.setAddress(addressDto);
        accommodationDto.setSizeType("Studio");

        AccommodationTypeDto accommodationTypeDto = new AccommodationTypeDto();
        accommodationTypeDto.setId(1L);
        accommodationTypeDto.setName("HOUSE");
        accommodationDto.setType(accommodationTypeDto);

        AmenityTypeDto amenityTypeDto = new AmenityTypeDto();
        amenityTypeDto.setId(1L);
        amenityTypeDto.setName("SWIMMING_POOL");

        AmenityTypeDto amenityTypeDto2 = new AmenityTypeDto();
        amenityTypeDto2.setId(2L);
        amenityTypeDto2.setName("GYM");

        accommodationDto.setAmenities(Set.of(amenityTypeDto, amenityTypeDto2));
        accommodationDto.setDailyRate(BigDecimal.valueOf(250.00).setScale(2, RoundingMode.HALF_UP));

        return accommodationDto;
    }

    public static AccommodationDto getAccommodationDto() {
        AccommodationDto accommodationDto = new AccommodationDto();
        accommodationDto.setId(1L);
        accommodationDto.setDailyRate(BigDecimal.valueOf(155, 2));
        accommodationDto.setSizeType("Test size");
        AddressDto addressDto = new AddressDto();
        addressDto.setCity("Test City");
        addressDto.setFloor("Test floor");
        addressDto.setCountry("Test country");
        addressDto.setState("Test state");
        addressDto.setStreet("Test street");
        addressDto.setApartmentNumber("Test number");
        addressDto.setHouseNumber("Test apartment number");
        addressDto.setZipCode("Test zipcode");
        accommodationDto.setAddress(addressDto);

        accommodationDto.setType(AccommodationTypeUtil.getAccommodationTypeDto());

        AmenityTypeDto amenityTypeDto = new AmenityTypeDto();
        amenityTypeDto.setId(1L);
        amenityTypeDto.setName("DISHWASHER");
        AmenityTypeDto amenityTypeDto2 = new AmenityTypeDto();
        amenityTypeDto2.setId(2L);
        amenityTypeDto.setName("SAFE");

        accommodationDto.setAmenities(Set.of(amenityTypeDto, amenityTypeDto2));

        return accommodationDto;
    }

    public static AccommodationDto getExpectedAccommodationDto() {
        AccommodationDto accommodationDto = new AccommodationDto();
        accommodationDto.setId(2L);
        AddressDto addressDto = new AddressDto();
        addressDto.setCountry("Ukraine");
        addressDto.setState("Lviv Region");
        addressDto.setCity("Lviv");
        addressDto.setStreet("Shevchenko");
        addressDto.setHouseNumber("33");
        addressDto.setApartmentNumber("3");
        addressDto.setFloor("3");
        addressDto.setZipCode("5353");
        accommodationDto.setAddress(addressDto);
        accommodationDto.setSizeType("Studio");
        AccommodationTypeDto accommodationTypeDto = new AccommodationTypeDto();
        accommodationTypeDto.setId(1L);
        accommodationTypeDto.setName("HOUSE");
        accommodationDto.setType(accommodationTypeDto);

        AmenityTypeDto amenityTypeDto = new AmenityTypeDto();
        amenityTypeDto.setId(1L);
        amenityTypeDto.setName("SWIMMING_POOL");

        AmenityTypeDto amenityTypeDto2 = new AmenityTypeDto();
        amenityTypeDto2.setId(2L);
        amenityTypeDto2.setName("GYM");

        accommodationDto.setAmenities(Set.of(amenityTypeDto, amenityTypeDto2));

        accommodationDto.setDailyRate(BigDecimal.valueOf(250.00).setScale(2, RoundingMode.HALF_UP));

        return accommodationDto;
    }

    public static AccommodationDto getExpectedAccommodationDto2() {
        AccommodationDto dto = new AccommodationDto();
        dto.setId(1L);
        AddressDto addressDto = new AddressDto();
        addressDto.setCountry("Ukraine");
        addressDto.setState("Lviv Region");
        addressDto.setCity("Lviv");
        addressDto.setStreet("Shevchenko");
        addressDto.setHouseNumber("33");
        addressDto.setApartmentNumber("3");
        addressDto.setFloor("3");
        addressDto.setZipCode("5353");
        dto.setAddress(addressDto);
        dto.setSizeType("SMALL");
        AccommodationTypeDto accommodationTypeDto = new AccommodationTypeDto();
        accommodationTypeDto.setId(1L);
        accommodationTypeDto.setName("HOUSE");
        dto.setType(accommodationTypeDto);

        AmenityTypeDto amenityTypeDto = new AmenityTypeDto();
        amenityTypeDto.setId(1L);
        amenityTypeDto.setName("SWIMMING_POOL");

        dto.setAmenities(Set.of(amenityTypeDto));

        dto.setDailyRate(BigDecimal.valueOf(250.00).setScale(2, RoundingMode.HALF_UP));

        return dto;
    }

    public static Accommodation getAccommodation() {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setSize("Test size");
        accommodation.setDailyRate(BigDecimal.valueOf(155, 2));
        accommodation.setDeleted(false);
        accommodation.setAvailability(5);

        accommodation.setType(AccommodationTypeUtil.getAccommodationType());

        accommodation.setAddress(AddressUtil.getAddress());

        accommodation.setAmenities(Set.of(AmenityTypeUtil.getAmenityType()));

        return accommodation;
    }
}
