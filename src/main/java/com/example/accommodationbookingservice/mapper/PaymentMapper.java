package com.example.accommodationbookingservice.mapper;

import com.example.accommodationbookingservice.config.MapperConfig;
import com.example.accommodationbookingservice.dto.payment.PaymentDetailsDto;
import com.example.accommodationbookingservice.dto.payment.PaymentDto;
import com.example.accommodationbookingservice.entity.payment.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {BookingMapper.class, PaymentStatusMapper.class})
public interface PaymentMapper {

    PaymentDto toDto(Payment payment);

    @Mapping(target = "bookingId", source = "booking.id")
    PaymentDetailsDto toInfoDto(Payment payment);

}
