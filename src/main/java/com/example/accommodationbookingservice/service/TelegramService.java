package com.example.accommodationbookingservice.service;

import com.example.accommodationbookingservice.dto.user.UserResponseDto;

public interface TelegramService {
    String getTelegramInviteUrl(String email);

    UserResponseDto auth(String token, Long chatId) throws Exception;
}
