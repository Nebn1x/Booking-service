package com.example.accommodationbookingservice.service.impl;

import com.example.accommodationbookingservice.dto.accommodation.AccommodationDto;
import com.example.accommodationbookingservice.dto.accommodation.AccommodationRequestDto;
import com.example.accommodationbookingservice.entity.accommodation.Accommodation;
import com.example.accommodationbookingservice.entity.accommodation.AccommodationType;
import com.example.accommodationbookingservice.entity.accommodation.Address;
import com.example.accommodationbookingservice.entity.accommodation.AmenityType;
import com.example.accommodationbookingservice.exception.EntityNotFoundException;
import com.example.accommodationbookingservice.mapper.AccommodationMapper;
import com.example.accommodationbookingservice.mapper.AddressMapper;
import com.example.accommodationbookingservice.repository.AccommodationRepository;
import com.example.accommodationbookingservice.repository.AccommodationTypeRepository;
import com.example.accommodationbookingservice.repository.AddressRepository;
import com.example.accommodationbookingservice.repository.AmenityTypeRepository;
import com.example.accommodationbookingservice.service.AccommodationService;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Transactional
@Service
@AllArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    public static final String ACCOMMODATION_TYPE_NOT_FOUND = "Accommodation type not found";
    public static final String CANT_FIND_ACCOMMODATION_BY_ID = "Cant find accommodation by id ";
    public static final String ACCOMMODATION_NOT_FOUND = "Accommodation not found";
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;
    private final AddressMapper addressMapper;
    private final AmenityTypeRepository amenityTypeRepository;
    private final AddressRepository addressRepository;
    private AccommodationTypeRepository accommodationTypeRepository;

    @Override
    public AccommodationDto save(AccommodationRequestDto requestDto) {
        Accommodation accommodation = new Accommodation();

        accommodation.setType(
                accommodationTypeRepository.findById(requestDto.getTypeId())
                        .orElseThrow(()
                                -> new EntityNotFoundException(ACCOMMODATION_TYPE_NOT_FOUND))
        );

        Address address = addressMapper.toEntity(requestDto.getAddressDto());
        addressRepository.save(address);
        accommodation.setAddress(address);

        accommodation.setSize(requestDto.getSizeType());

        Set<AmenityType> amenities = new HashSet<>(amenityTypeRepository
                .findAllById(requestDto.getAmenityTypeIds()));
        accommodation.setAmenities(amenities);

        accommodation.setDailyRate(requestDto.getDailyRate());
        accommodation.setAvailability(requestDto.getAvailability());

        return accommodationMapper.toDto(accommodationRepository.save(accommodation));
    }

    @Override
    public AccommodationDto getById(Long id) {
        return accommodationMapper.toDto(accommodationRepository
                .findById(id).orElseThrow(
                        () -> new EntityNotFoundException(CANT_FIND_ACCOMMODATION_BY_ID + id)));
    }

    @Override
    public List<AccommodationDto> findAll(Pageable pageable) {
        return accommodationRepository.findAll(pageable).stream()
                .map(accommodationMapper::toDto)
                .toList();
    }

    @Override
    public AccommodationDto update(Long id, AccommodationRequestDto requestDto) {
        Accommodation accommodation = accommodationRepository
                .findById(id).orElseThrow(() ->
                        new EntityNotFoundException(CANT_FIND_ACCOMMODATION_BY_ID));

        accommodationMapper.updateFromDto(requestDto, accommodation);

        AccommodationType type = accommodationTypeRepository.findById(requestDto.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException(ACCOMMODATION_TYPE_NOT_FOUND));
        accommodation.setType(type);

        accommodation.setSize(requestDto.getSizeType());

        Set<AmenityType> amenities = new HashSet<>(amenityTypeRepository
                .findAllById(requestDto.getAmenityTypeIds()));
        accommodation.setAmenities(amenities);

        Address address = addressMapper.toEntity(requestDto.getAddressDto());
        address.setId(accommodation.getAddress().getId());
        addressRepository.save(address);
        accommodation.setAddress(address);

        Accommodation updated = accommodationRepository.save(accommodation);
        return accommodationMapper.toDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        if (!accommodationRepository.existsById(id)) {
            throw new EntityNotFoundException(ACCOMMODATION_NOT_FOUND);
        }
        accommodationRepository.deleteById(id);
    }
}
