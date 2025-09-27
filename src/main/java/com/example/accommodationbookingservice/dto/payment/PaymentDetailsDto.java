package com.example.accommodationbookingservice.dto.payment;

import com.example.accommodationbookingservice.dto.paymentstatus.PaymentStatusDto;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDetailsDto {
    private Long id;
    private PaymentStatusDto status;
    private Long bookingId;
    private BigDecimal amount;
}
