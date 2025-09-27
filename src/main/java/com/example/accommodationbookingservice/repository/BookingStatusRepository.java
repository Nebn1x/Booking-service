package com.example.accommodationbookingservice.repository;

import com.example.accommodationbookingservice.entity.booking.BookingStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingStatusRepository extends JpaRepository<BookingStatus, Long> {

    Optional<BookingStatus> findByName(BookingStatus.BookingStatusName name);

}
