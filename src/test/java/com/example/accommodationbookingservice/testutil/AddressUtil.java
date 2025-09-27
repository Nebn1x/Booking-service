package com.example.accommodationbookingservice.testutil;

import com.example.accommodationbookingservice.entity.accommodation.Address;

public class AddressUtil {

    public static Address getAddress() {
        Address address = new Address();
        address.setId(1L);
        address.setDeleted(false);
        address.setCity("Test City");
        address.setFloor("Test floor");
        address.setCountry("Test country");
        address.setState("Test state");
        address.setStreet("Test street");
        address.setApartmentNumber("Test number");
        address.setHouseNumber("Test apartment number");
        address.setZipCode("Test zipcode");

        return address;
    }
}
