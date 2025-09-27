package com.example.accommodationbookingservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.accommodationbookingservice.dto.roletype.RoleTypeDto;
import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import com.example.accommodationbookingservice.dto.user.UserUpdatePasswordRequestDto;
import com.example.accommodationbookingservice.dto.user.UserUpdateRequestDto;
import com.example.accommodationbookingservice.dto.user.UserUpdateRoleRequestDto;
import com.example.accommodationbookingservice.entity.user.RoleName;
import com.example.accommodationbookingservice.testutil.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String DELETE_TEST_USER_SQL = "database/delete/delete-test-user.sql";
    private static final String INSERT_TEST_USER_SQL = "database/user/insert-test-user.sql";
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext,
                          @Autowired DataSource dataSource) {
        teardown(dataSource);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_TEST_USER_SQL));
        }
    }

    @Test
    @WithMockUser(username = "test@gmail.com", authorities = {"CUSTOMER"})
    @DisplayName("Should return profile info for authenticated CUSTOMER user")
    void getInfo_AuthenticatedCustomer_ShouldReturnUserProfile() throws Exception {
        UserResponseDto expectedUser = UserUtil.getUserResponseDtoCustomer();

        MvcResult result = mockMvc.perform(get("/user/me", "test@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto actualUser = objectMapper
                .readValue(result.getResponse().getContentAsString(), UserResponseDto.class);

        assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser);
    }

    @Test
    @WithMockUser(username = "test@gmail.com", authorities = {"CUSTOMER"})
    @DisplayName("Should update user profile when request is valid and user is CUSTOMER")
    void updateProfile_ValidRequestByCustomer_ShouldReturnUpdatedUser() throws Exception {
        UserUpdateRequestDto userUpdateRequestDto = UserUtil.getUserUpdateRequestDto();
        String jsonValue = objectMapper.writeValueAsString(userUpdateRequestDto);
        UserResponseDto expectedUser = UserUtil.getUpdatedUserWithRole();

        MvcResult result = mockMvc.perform(patch("/user/me")
                        .content(jsonValue)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto actualUser = objectMapper
                .readValue(result.getResponse().getContentAsString(), UserResponseDto.class);

        assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser);
    }

    @Test
    @WithMockUser(username = "test@gmail.com", authorities = {"CUSTOMER"})
    @DisplayName("Should update password when old password "
            + "is correct and new password is confirmed")
    void updatePassword_ValidPasswordChangeRequest_ShouldReturnSuccessMessage() throws Exception {
        UserUpdatePasswordRequestDto requestDto = UserUtil.getUserUpdateHashedPasswordRequestDto();
        String jsonValue = objectMapper.writeValueAsString(requestDto);
        String expected = "Password updated successfully";

        System.out.println(jsonValue);

        MvcResult result = mockMvc.perform(put("/user/me/password")
                        .content(jsonValue)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualResult = result.getResponse().getContentAsString();

        assertEquals(expected, actualResult);
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"ADMIN"})
    @DisplayName("Should update user role when called by ADMIN with valid request")
    void updateRoles_ValidRequestByAdmin_ShouldUpdateUserRole() throws Exception {
        Long id = 3L;
        UserUpdateRoleRequestDto requestDto = UserUtil.getUserUpdateRoleRequestDto();
        String jsonValue = objectMapper.writeValueAsString(requestDto);
        UserResponseDto expected = UserUtil.getUserResponseDtoAdmin();
        RoleName expectedRoleName = RoleName.ADMIN;

        MvcResult result = mockMvc.perform(patch("/user/{id}/role", id)
                        .content(jsonValue)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto actualUser =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        UserResponseDto.class);

        assertTrue(actualUser.getRoles().stream().findFirst().isPresent());
        RoleTypeDto roleTypeDto = actualUser.getRoles().stream().findFirst().get();

        assertEquals(roleTypeDto.getName(), expectedRoleName.name());
        assertThat(actualUser).usingRecursiveComparison()
                .ignoringFields("roles")
                .isEqualTo(expected);
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(DELETE_TEST_USER_SQL));
        }
    }
}
