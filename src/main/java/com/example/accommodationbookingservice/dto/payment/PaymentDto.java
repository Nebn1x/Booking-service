package com.example.accommodationbookingservice.dto.payment;

import com.example.accommodationbookingservice.dto.paymentstatus.PaymentStatusDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {
    private Long id;
    private PaymentStatusDto status;
    private String sessionId;
}
