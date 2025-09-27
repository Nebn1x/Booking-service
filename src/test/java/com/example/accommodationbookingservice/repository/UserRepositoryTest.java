package com.example.accommodationbookingservice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.accommodationbookingservice.entity.user.User;
import com.example.accommodationbookingservice.testutil.UserUtil;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should return user with roles when user with given email exists")
    void findUserByEmail_ExistingEmail_ShouldReturnUserWithRoles() {
        User expectedUser = UserUtil.getExpectedUser();
        String email = "customer@example.com";

        Optional<User> actualResult = userRepository.findUserByEmail(email);

        assertTrue(actualResult.isPresent());
        User actualUser = actualResult.get();
        assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("Should return empty Optional when user with given email does not exist")
    void findUserByEmail_NonExistingEmail_ShouldReturnEmptyOptional() {
        String nonExistingEmail = "unknown@example.com";

        Optional<User> actualResult = userRepository.findUserByEmail(nonExistingEmail);

        assertTrue(actualResult.isEmpty());
    }

    @Test
    @DisplayName("Should return user with roles when user with given ID exists")
    void findUserById_ExistingId_ShouldReturnUserWithRoles() {
        User expectedUser = UserUtil.getExpectedUser();
        Long id = expectedUser.getId();

        Optional<User> actualResult = userRepository.findUserById(id);

        assertTrue(actualResult.isPresent());
        User actualUser = actualResult.get();
        assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("Should return true when user with given email exists")
    void existsByEmail_ExistingEmail_ShouldReturnTrue() {
        String email = "customer@example.com";

        boolean result = userRepository.existsByEmail(email);

        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when user with given email does not exist")
    void existsByEmail_NonExistingEmail_ShouldReturnFalse() {
        String email = "nonexistent@example.com";

        boolean result = userRepository.existsByEmail(email);

        assertFalse(result);
    }
}
