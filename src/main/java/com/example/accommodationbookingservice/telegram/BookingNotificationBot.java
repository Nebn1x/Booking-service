package com.example.accommodationbookingservice.telegram;

import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import com.example.accommodationbookingservice.entity.telegram.TelegramChat;
import com.example.accommodationbookingservice.exception.InvalidTelegramToken;
import com.example.accommodationbookingservice.exception.TelegramBotException;
import com.example.accommodationbookingservice.repository.TelegramChatRepository;
import com.example.accommodationbookingservice.service.TelegramService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class BookingNotificationBot extends TelegramLongPollingBot {

    public static final String TELEGRAM_BOT_EXCEPTION = "Telegram bot exception: ";
    public static final String MASSAGE_DONT_HAVE_TOKEN = "Massage dont have token";
    public static final String REGEX = " ";
    public static final String START = "/start";
    public static final String CANT_DECODE_TOKEN = "Cant decode token: ";
    private final TelegramService telegramService;
    private final TelegramChatRepository telegramChatRepository;

    @Value("${telegram.bot.username}")
    private String botUserName;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (messageText.startsWith(START)) {
                String token = getToken(messageText);
                try {
                    UserResponseDto userDto = telegramService.auth(token, chatId);
                    sendMessage(chatId,
                            TelegramNotificationBuilder.bookingStarted(userDto.getFirstName()));
                } catch (Exception e) {
                    sendMessage(chatId, TelegramNotificationBuilder.dontValidToken());
                    throw new InvalidTelegramToken(CANT_DECODE_TOKEN + token);
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private String getToken(String message) {
        String[] tokens = message.split(REGEX);
        if (tokens.length == 2) {
            return tokens[1];
        }
        throw new InvalidTelegramToken(MASSAGE_DONT_HAVE_TOKEN);
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramBotException(TELEGRAM_BOT_EXCEPTION + e.getMessage());
        }
    }

    public void sendMessage(String email, String text) {
        Optional<Long> optionalOfChatId = getChatId(email);
        if (optionalOfChatId.isPresent()) {
            Long chatId = optionalOfChatId.get();
            sendMessage(chatId, text);
        }
    }

    private Optional<Long> getChatId(String email) {
        Optional<TelegramChat> optionalOfChat
                = telegramChatRepository.getTelegramChatByUserEmail(email);
        return optionalOfChat.flatMap(telegramChat -> telegramChat.getChatId().describeConstable());
    }
}
