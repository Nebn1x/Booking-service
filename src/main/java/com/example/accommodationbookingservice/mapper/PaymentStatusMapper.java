package com.example.accommodationbookingservice.mapper;

import com.example.accommodationbookingservice.config.MapperConfig;
import com.example.accommodationbookingservice.dto.paymentstatus.PaymentStatusDto;
import com.example.accommodationbookingservice.entity.payment.PaymentStatus;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PaymentStatusMapper {
    PaymentStatusDto toDto(PaymentStatus paymentStatus);
}
