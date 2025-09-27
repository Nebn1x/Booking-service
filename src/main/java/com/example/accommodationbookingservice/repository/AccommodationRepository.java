package com.example.accommodationbookingservice.repository;

import com.example.accommodationbookingservice.entity.accommodation.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {}
