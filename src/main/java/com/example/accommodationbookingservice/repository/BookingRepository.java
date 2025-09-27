package com.example.accommodationbookingservice.repository;

import com.example.accommodationbookingservice.entity.booking.Booking;
import com.example.accommodationbookingservice.entity.booking.BookingStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT COUNT(b) FROM Booking b "
            + "WHERE b.accommodation.id = :accommodationId "
            + "AND NOT b.status.name = 'CANCELED' "
            + "AND NOT b.status.name = 'EXPIRED' "
            + "AND ("
            + "(:checkInDate BETWEEN b.checkInDate AND b.checkOutDate "
            + "OR :checkOutDate BETWEEN b.checkInDate AND b.checkOutDate) "
            + "OR (b.checkInDate BETWEEN :checkInDate AND :checkOutDate "
            + "OR b.checkOutDate BETWEEN :checkInDate AND :checkOutDate))")
    Long isDatesAvailableForAccommodation(@Param("accommodationId") Long accommodationId,
                                          @Param("checkInDate") LocalDate checkInDate,
                                          @Param("checkOutDate") LocalDate checkOutDate);

    List<Booking> findAllByUser_Email(String email, Pageable pageable);

    List<Booking> findByUserId(Long id);

    @Query("select b from Booking b "
            + "where b.checkInDate = :checkInDate and b.status.name = :statusName")
    List<Booking> findAllByCheckInDate(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("statusName") BookingStatus.BookingStatusName statusName);

    @Query("select b from Booking b "
            + "where b.bookingCreatedAt < :dateTime "
            + "and b.status.name = :statusName")
    List<Booking> findAllByCreatedAtAndStatus(
            @Param("dateTime") LocalDateTime checkOutDate,
            @Param("statusName") BookingStatus.BookingStatusName statusName);

    @Query("select b from Booking b "
            + "where b.checkOutDate = :checkOutDate "
            + "and b.status.name = :statusName")
    List<Booking> findByCheckOutDateAndStatus(
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("statusName") BookingStatus.BookingStatusName statusName);
}
