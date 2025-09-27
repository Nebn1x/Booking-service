package com.example.accommodationbookingservice.service;

import com.example.accommodationbookingservice.dto.payment.PaymentDetailsDto;
import com.example.accommodationbookingservice.dto.payment.PaymentDto;
import java.util.List;

public interface PaymentService {
    String createPaymentCheckoutSession(Long id, String email);

    PaymentDto successPayment(String sessionId);

    PaymentDto cancelPaymentAndBooking(String sessionId);

    List<PaymentDetailsDto> findPaymentsByUserEmail(String email);

    List<PaymentDetailsDto> findAllPayments();
}
