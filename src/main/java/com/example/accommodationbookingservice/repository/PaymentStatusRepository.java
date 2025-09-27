package com.example.accommodationbookingservice.repository;

import com.example.accommodationbookingservice.entity.payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
    PaymentStatus findPaymentStatusByName(PaymentStatus.PaymentStatusName name);

}
