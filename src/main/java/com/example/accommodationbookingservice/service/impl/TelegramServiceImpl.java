package com.example.accommodationbookingservice.service.impl;

import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import com.example.accommodationbookingservice.entity.telegram.TelegramChat;
import com.example.accommodationbookingservice.entity.user.User;
import com.example.accommodationbookingservice.exception.EntityNotFoundException;
import com.example.accommodationbookingservice.mapper.UserMapper;
import com.example.accommodationbookingservice.repository.TelegramChatRepository;
import com.example.accommodationbookingservice.repository.UserRepository;
import com.example.accommodationbookingservice.service.TelegramService;
import com.example.accommodationbookingservice.telegram.EmailTokenService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TelegramServiceImpl implements TelegramService {

    private static final String URL = "https://t.me/QuickBooker_bot?start=";
    private static final String CANNOT_FIND_USER_WITH_EMAIL = "Cannot find user with email: ";
    private final EmailTokenService emailTokenService;
    private final UserRepository userRepository;
    private final TelegramChatRepository telegramChatRepository;
    private final UserMapper userMapper;

    @Override
    public String getTelegramInviteUrl(String email) {
        String token = emailTokenService.encryptEmail(email);
        return URL + token;
    }

    @Override
    @Transactional
    public UserResponseDto auth(String token, Long chatId) throws Exception {
        String email = emailTokenService.decryptEmail(token);
        Optional<UserResponseDto> optionalUser = ifUserAuthorize(email);
        return optionalUser.orElseGet(() -> createUserAndChat(email, chatId));
    }

    private Optional<UserResponseDto> ifUserAuthorize(String email) {
        Optional<TelegramChat> chat =
                telegramChatRepository.getTelegramChatByUserEmail(email);
        if (chat.isPresent()) {
            User user = chat.get().getUser();
            return Optional.of(userMapper.toResponseDto(user));
        }
        return Optional.empty();
    }

    private UserResponseDto createUserAndChat(String email, Long chatId) {
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(CANNOT_FIND_USER_WITH_EMAIL + email));
        TelegramChat telegramChat = new TelegramChat();
        telegramChat.setUser(user);
        telegramChat.setChatId(chatId);
        telegramChatRepository.save(telegramChat);
        return userMapper.toResponseDto(user);
    }
}
