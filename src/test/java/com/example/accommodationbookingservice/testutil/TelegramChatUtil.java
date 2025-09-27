package com.example.accommodationbookingservice.testutil;

import com.example.accommodationbookingservice.entity.telegram.TelegramChat;

public class TelegramChatUtil {

    public static TelegramChat getTelegramChat() {
        TelegramChat telegramChat = new TelegramChat();
        telegramChat.setChatId(1L);
        telegramChat.setId(1L);
        telegramChat.setUser(UserUtil.getUser());
        telegramChat.setDeleted(false);

        return telegramChat;
    }

    public static TelegramChat expectedTelegramCHat() {
        TelegramChat telegramChat = new TelegramChat();
        telegramChat.setChatId(12345L);
        telegramChat.setId(1L);
        telegramChat.setDeleted(false);

        return telegramChat;
    }
}
