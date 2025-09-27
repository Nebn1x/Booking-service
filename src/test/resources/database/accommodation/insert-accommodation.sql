INSERT INTO addresses (id, country, state, city, street, house_number, apartment_number, floor, zip_code, is_deleted)
VALUES (1, 'Ukraine', 'Lviv Region', 'Lviv', 'Shevchenko', 33, 3, 3, '5353', false);
INSERT INTO accommodations (id, type_id, address_id, size, daily_rate, availability, is_deleted)
VALUES (1, 1, 1, 'SMALL', 250, 10, false);
INSERT INTO accommodations_amenity_types(accommodation_id, amenity_type_id)
VALUES (1, 1)



