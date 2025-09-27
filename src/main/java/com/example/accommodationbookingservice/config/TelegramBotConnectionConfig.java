package com.example.accommodationbookingservice.config;

import com.example.accommodationbookingservice.exception.TelegramBotException;
import com.example.accommodationbookingservice.telegram.BookingNotificationBot;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Profile("!test")
@RequiredArgsConstructor
public class TelegramBotConnectionConfig {

    public static final String TELEGRAM_EXCEPTION = "Telegram exception ";
    private final BookingNotificationBot bookingNotificationBot;

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bookingNotificationBot);
        } catch (TelegramApiException e) {
            throw new TelegramBotException(TELEGRAM_EXCEPTION + e.getMessage());
        }
    }
}
