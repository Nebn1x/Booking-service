package com.example.accommodationbookingservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import com.example.accommodationbookingservice.entity.telegram.TelegramChat;
import com.example.accommodationbookingservice.entity.user.User;
import com.example.accommodationbookingservice.exception.EntityNotFoundException;
import com.example.accommodationbookingservice.mapper.UserMapper;
import com.example.accommodationbookingservice.repository.TelegramChatRepository;
import com.example.accommodationbookingservice.repository.UserRepository;
import com.example.accommodationbookingservice.telegram.EmailTokenService;
import com.example.accommodationbookingservice.testutil.TelegramChatUtil;
import com.example.accommodationbookingservice.testutil.UserUtil;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TelegramServiceImplTest {

    @Mock
    private EmailTokenService emailTokenService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TelegramChatRepository telegramChatRepository;
    @Mock
    private UserMapper userMapper;
    private TelegramChat telegramChat;
    private String url;
    private String token;
    private User user;
    private UserResponseDto userResponseDto;
    private String email;

    @InjectMocks
    private TelegramServiceImpl telegramService;

    @BeforeEach
    void setUp() {
        url = "https://t.me/QuickBooker_bot?start=";
        token = "testToken";
        email = "test@example.com";
        telegramChat = TelegramChatUtil.getTelegramChat();
        user = UserUtil.getUser();
        userResponseDto = UserUtil.getUserResponseDto();
    }

    @Test
    @DisplayName("Should return valid Telegram invite URL when email is provided")
    void getTelegramInviteUrl_ValidEmail_ShouldReturnUrl() {
        when(emailTokenService.encryptEmail(email)).thenReturn(token);
        String actualResult = telegramService.getTelegramInviteUrl(email);

        String expectedResult = url + token;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Should authenticate user if already authorized "
            + "and return UserResponseDto")
    void auth_TokenBelongsToExistingUser_ShouldReturnUserResponseDto() throws Exception {
        final UserResponseDto expectedUser = userResponseDto;

        when(emailTokenService.decryptEmail(token)).thenReturn(email);
        when(telegramChatRepository.getTelegramChatByUserEmail(email))
                .thenReturn(Optional.of(telegramChat));
        when(userMapper.toResponseDto(any())).thenReturn(userResponseDto);

        UserResponseDto actualUser = telegramService.auth(token, 1L);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("Should throw an exception because the Telegram chat does not exist")
    void auth_TelegramChatNoExist_ShouldReturnException() throws Exception {
        when(emailTokenService.decryptEmail(token)).thenReturn(email);
        EntityNotFoundException actualException = assertThrows(EntityNotFoundException.class,
                () -> telegramService.auth(token, 1L));

        String expectedMessage = "Cannot find user with email: " + email;

        assertEquals(expectedMessage, actualException.getMessage());
    }
}
