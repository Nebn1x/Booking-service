package com.example.accommodationbookingservice.service.impl;

import static com.example.accommodationbookingservice.testutil.AccommodationUtil.getAccommodation;
import static com.example.accommodationbookingservice.testutil.AccommodationUtil.getAccommodationDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
import com.example.accommodationbookingservice.testutil.AccommodationTypeUtil;
import com.example.accommodationbookingservice.testutil.AccommodationUtil;
import com.example.accommodationbookingservice.testutil.AddressUtil;
import com.example.accommodationbookingservice.testutil.AmenityTypeUtil;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceImplTest {

    @Mock
    private AccommodationRepository accommodationRepository;
    @Mock
    private AccommodationMapper accommodationMapper;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private AmenityTypeRepository amenityTypeRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AccommodationTypeRepository accommodationTypeRepository;
    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    private Accommodation accommodation;
    private AccommodationDto accommodationDto;
    private AccommodationType accommodationType;
    private Address address;
    private AmenityType amenityType;
    private AccommodationRequestDto accommodationRequestDto;

    @BeforeEach
    void setUp() {
        accommodation = getAccommodation();
        accommodationRequestDto = AccommodationUtil.createSampleAccommodationRequestDto();
        accommodationDto = getAccommodationDto();
        accommodationType = AccommodationTypeUtil.getAccommodationType();
        address = AddressUtil.getAddress();
        amenityType = AmenityTypeUtil.getAmenityType();
    }

    @Test
    @DisplayName("Save accommodation with valid values")
    void save_ValidData_ShouldReturnAccommodationDto() {
        AccommodationDto expected = AccommodationUtil.getAccommodationDto();
        when(accommodationTypeRepository.findById(1L))
                .thenReturn(Optional.of(accommodation.getType()));

        when(addressMapper.toEntity(accommodationRequestDto.getAddressDto()))
                .thenReturn(accommodation.getAddress());

        when(addressRepository.save(accommodation.getAddress()))
                .thenReturn(accommodation.getAddress());

        when(accommodationMapper.toDto(accommodationRepository.save(accommodation)))
                .thenReturn(accommodationDto);

        AccommodationDto actual = accommodationService.save(accommodationRequestDto);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Save accommodation with invalid values")
    void save_NotValidData_ShouldReturnException() {
        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, ()
                        -> accommodationService.save(accommodationRequestDto));
        String expectedMessage = "Accommodation type not found";
        assertEquals(expectedMessage, entityNotFoundException.getMessage());
    }

    @Test
    @DisplayName("Get accommodation by id")
    void getById_ExistingAccommodation_ShouldReturnAccommodationDto() {
        final AccommodationDto expected = accommodationDto;
        Long id = 1L;

        when(accommodationRepository.findById(id)).thenReturn(Optional.of(accommodation));
        when(accommodationMapper.toDto(accommodation)).thenReturn(accommodationDto);

        AccommodationDto actualAccommodationById = accommodationService.getById(id);

        assertNotNull(actualAccommodationById);
        assertThat(actualAccommodationById).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Save accommodation with invalid values")
    void getById_NotExistingAccommodation_ShouldReturnException() {
        Long id = 999L;
        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, ()
                        -> accommodationService.getById(id));
        String expectedMessage = "Cant find accommodation by id " + id;
        assertEquals(expectedMessage, entityNotFoundException.getMessage());
    }

    @Test
    @DisplayName("Find all existing accommodations")
    void findAll_ExistingAccommodation_ShouldReturnListAccommodationDto() {
        List<Accommodation> accommodationList = List.of(accommodation);
        Pageable pageable = PageRequest.of(1, 1);
        Page<Accommodation> accommodationPage = new PageImpl<>(accommodationList);

        when(accommodationMapper.toDto(accommodation)).thenReturn(accommodationDto);
        when(accommodationRepository.findAll(pageable)).thenReturn(accommodationPage);

        List<AccommodationDto> actual = accommodationService.findAll(pageable);
        AccommodationDto actualAccommodation = actual.getFirst();

        assertThat(actualAccommodation).usingRecursiveComparison().isEqualTo(accommodationDto);
    }

    @Test
    @DisplayName("Update existing accommodation and return accommodation dto")
    void update_ExistingAccommodation_ShouldReturnAccommodationDto() {
        Long id = 1L;
        AccommodationDto expected = accommodationDto;
        when(accommodationRepository.findById(id)).thenReturn(Optional.of(accommodation));
        when(accommodationTypeRepository.findById(id)).thenReturn(Optional.of(accommodationType));
        when(addressMapper.toEntity(accommodationRequestDto.getAddressDto())).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);
        when(accommodationMapper.toDto(accommodation)).thenReturn(accommodationDto);
        when(amenityTypeRepository.findAllById(accommodationRequestDto.getAmenityTypeIds()))
                .thenReturn(List.of(amenityType));
        when(accommodationRepository.save(accommodation)).thenReturn(accommodation);
        AccommodationDto actualUpdate = accommodationService.update(id, accommodationRequestDto);
        assertThat(actualUpdate).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Update a non-existing accommodation")
    void update_NonExistingAccommodation_ShouldReturnException() {
        Long id = 999L;
        EntityNotFoundException actualMessage = assertThrows(EntityNotFoundException.class,
                () -> accommodationService.update(id, accommodationRequestDto));

        String expectedMessage = "Cant find accommodation by id ";

        assertEquals(expectedMessage, actualMessage.getMessage());
    }

    @Test
    @DisplayName("Delete existing accommodation")
    void delete_ExistingAccommodation_ShouldDeleteAccommodation() {
        Long id = 1L;
        when(accommodationRepository.existsById(id)).thenReturn(true);
        accommodationService.deleteById(id);
    }

    @Test
    @DisplayName("Delete a non-existing accommodation")
    void delete_NonExistingAccommodation_ShouldDeleteAccommodation() {
        Long id = 99L;
        EntityNotFoundException actualMessage = assertThrows(EntityNotFoundException.class,
                () -> accommodationService.deleteById(id));
        String expectedMessage = "Accommodation not found";
        assertEquals(expectedMessage, actualMessage.getMessage());
    }
}
