package com.example.accommodationbookingservice.testutil;

import com.example.accommodationbookingservice.dto.payment.PaymentDetailsDto;
import com.example.accommodationbookingservice.dto.payment.PaymentDto;
import com.example.accommodationbookingservice.entity.payment.Payment;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaymentUtil {

    public static Payment getPayment() {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setBooking(BookingUtil.getPendingBooking());
        payment.setAmount(BigDecimal.valueOf(155, 2));
        payment.setStatus(PaymentStatusUtil.getPaymentStatus());
        payment.setDeleted(false);
        payment.setSessionId("test payment session id");
        payment.setSessionUrl("/session-test-payment-url");

        return payment;
    }

    public static PaymentDto getPaymentDto() {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setId(1L);
        paymentDto.setStatus(PaymentStatusUtil.getPaymentStatusDto());
        paymentDto.setSessionId("test payment session id");

        return paymentDto;
    }

    public static PaymentDetailsDto getPaymentDetailsDto() {
        PaymentDetailsDto paymentDetailsDto = new PaymentDetailsDto();
        paymentDetailsDto.setId(1L);
        paymentDetailsDto.setStatus(PaymentStatusUtil.getPaymentStatusDto());
        paymentDetailsDto.setBookingId(1L);
        paymentDetailsDto.setAmount(BigDecimal.valueOf(155, 2));

        return paymentDetailsDto;
    }

    public static Payment getExpectedPayment() {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setSessionUrl("session/url");
        payment.setSessionId("testSessionId1");
        payment.setAmount(BigDecimal.valueOf(2000).setScale(2, RoundingMode.HALF_UP));

        return payment;
    }
}
