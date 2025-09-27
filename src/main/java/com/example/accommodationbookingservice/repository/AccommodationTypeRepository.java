package com.example.accommodationbookingservice.repository;

import com.example.accommodationbookingservice.entity.accommodation.AccommodationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationTypeRepository extends JpaRepository<AccommodationType, Long> {}
