package com.example.accommodationbookingservice.repository;

import com.example.accommodationbookingservice.entity.accommodation.AmenityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenityTypeRepository extends JpaRepository<AmenityType, Long> {}
