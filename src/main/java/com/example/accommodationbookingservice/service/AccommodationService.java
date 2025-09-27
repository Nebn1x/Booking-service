package com.example.accommodationbookingservice.service;

import com.example.accommodationbookingservice.dto.accommodation.AccommodationDto;
import com.example.accommodationbookingservice.dto.accommodation.AccommodationRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AccommodationService {

    AccommodationDto save(AccommodationRequestDto requestDto);

    AccommodationDto getById(Long id);

    List<AccommodationDto> findAll(Pageable pageable);

    AccommodationDto update(Long id, AccommodationRequestDto requestDto);

    void deleteById(Long id);
}
