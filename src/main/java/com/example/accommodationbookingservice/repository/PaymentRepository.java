package com.example.accommodationbookingservice.repository;

import com.example.accommodationbookingservice.entity.payment.Payment;
import com.example.accommodationbookingservice.entity.payment.PaymentStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findPaymentByBookingId(Long bookingId);

    @Query("select p from Payment p where p.sessionId = :sessionId")
    Optional<Payment> findPaymentBySessionId(@Param("sessionId") String sessionId);

    List<Payment> findPaymentByBookingUserEmail(String email);

    List<Payment> findAllByBookingIdInAndStatus_Name(List<Long> bookingIds,
                                                     PaymentStatus.PaymentStatusName statusName);
}
