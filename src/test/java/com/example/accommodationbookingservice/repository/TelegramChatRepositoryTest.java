package com.example.accommodationbookingservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.accommodationbookingservice.entity.telegram.TelegramChat;
import com.example.accommodationbookingservice.entity.user.RoleName;
import com.example.accommodationbookingservice.testutil.TelegramChatUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TelegramChatRepositoryTest {

    private static final RoleName CUSTOMER = RoleName.CUSTOMER;
    private static final String DELETE_ALL_SQL = "database/delete/delete-all.sql";
    private static final String INSERT_TELEGRAMCHATS_SQL =
            "database/tgchat/insert-telegramchats.sql";

    @Autowired
    private TelegramChatRepository telegramChatRepository;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_TELEGRAMCHATS_SQL));
        }
    }

    @Test
    @DisplayName("Should return TelegramChat when user with given email exists")
    void getTelegramChatByUserEmail_ExistingEmail_ShouldReturnChat() {
        TelegramChat expectedTelegramChat = TelegramChatUtil.expectedTelegramCHat();
        String email = "customer@example.com";

        Optional<TelegramChat> actualResult = telegramChatRepository
                .getTelegramChatByUserEmail(email);

        assertTrue(actualResult.isPresent());
        TelegramChat actualTelegramChat = actualResult.get();

        assertFalse(actualTelegramChat.isDeleted());
        assertEquals(expectedTelegramChat.getId(), actualTelegramChat.getId());
        assertEquals(expectedTelegramChat.getChatId(), actualTelegramChat.getChatId());
    }

    @Test
    @DisplayName("Should return empty Optional when no TelegramChat found for given email")
    void getTelegramChatByUserEmail_NonExistingEmail_ShouldReturnEmptyOptional() {
        String nonExistingEmail = "notfound@example.com";

        Optional<TelegramChat> actualResult = telegramChatRepository
                .getTelegramChatByUserEmail(nonExistingEmail);

        assertTrue(actualResult.isEmpty());
    }

    @Test
    @DisplayName("Should return TelegramChats when user has the specified role")
    void findAllByUserRolesRole_ExistingRole_ShouldReturnChats() {
        TelegramChat expectedTelegramChat = TelegramChatUtil.expectedTelegramCHat();

        List<TelegramChat> actualResult = telegramChatRepository.findAllByUser_Roles_Role(CUSTOMER);

        assertFalse(actualResult.isEmpty());
        TelegramChat actualTelegramChat = actualResult.getFirst();

        assertFalse(actualTelegramChat.isDeleted());
        assertEquals(expectedTelegramChat.getId(), actualTelegramChat.getId());
        assertEquals(expectedTelegramChat.getChatId(), actualTelegramChat.getChatId());
        assertTrue(actualTelegramChat.getUser().getRoles().stream()
                .anyMatch(role -> role.getRole().equals(CUSTOMER)));
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(DELETE_ALL_SQL));
        }
    }
}
