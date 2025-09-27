package com.example.accommodationbookingservice.testutil;

import com.example.accommodationbookingservice.dto.paymentstatus.PaymentStatusDto;
import com.example.accommodationbookingservice.entity.payment.PaymentStatus;

public class PaymentStatusUtil {

    public static PaymentStatus getPaymentStatus() {
        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setId(1L);
        paymentStatus.setName(PaymentStatus.PaymentStatusName.PENDING);
        return paymentStatus;
    }

    public static PaymentStatus getPaymentStatusPaid() {
        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setId(1L);
        paymentStatus.setName(PaymentStatus.PaymentStatusName.PAID);
        return paymentStatus;
    }

    public static PaymentStatusDto getPaymentStatusDto() {
        PaymentStatusDto paymentStatusDto = new PaymentStatusDto();
        paymentStatusDto.setId(1L);
        paymentStatusDto.setName("PAID");
        return paymentStatusDto;
    }
}

