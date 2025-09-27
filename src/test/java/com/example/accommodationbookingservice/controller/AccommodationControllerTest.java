package com.example.accommodationbookingservice.controller;

import static com.example.accommodationbookingservice.testutil.AccommodationUtil.createSampleAccommodationRequestDto;
import static com.example.accommodationbookingservice.testutil.AccommodationUtil.getAccommodationDtoAfterUpdate;
import static com.example.accommodationbookingservice.testutil.AccommodationUtil.getAccommodationUpdateRequestDto;
import static com.example.accommodationbookingservice.testutil.AccommodationUtil.getExpectedAccommodationDto;
import static com.example.accommodationbookingservice.testutil.AccommodationUtil.getExpectedAccommodationDto2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.accommodationbookingservice.dto.accommodation.AccommodationDto;
import com.example.accommodationbookingservice.dto.accommodation.AccommodationRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
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
class AccommodationControllerTest {

    public static final String DELETE_ALL_SQL = "database/delete/delete-all.sql";
    public static final String INSERT_ACCOMMODATION_SQL =
            "database/accommodation/insert-accommodation.sql";
    public static final String BOOKING_INSERT_BOOKING_SQL = "database/booking/insert-booking.sql";
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
                    new ClassPathResource(INSERT_ACCOMMODATION_SQL));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(BOOKING_INSERT_BOOKING_SQL));
        }
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"ADMIN"})
    @DisplayName("Should create a new accommodation unit when "
            + "request is valid and user has ADMIN role")
    void save_ValidRequestWithAdminRole_ShouldCreateAccommodation() throws Exception {
        AccommodationRequestDto requestDto = createSampleAccommodationRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        AccommodationDto expectedAccommodationDto = getExpectedAccommodationDto();

        MvcResult result = mockMvc.perform(post("/accommodations")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        AccommodationDto actualDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), AccommodationDto.class);

        assertEquals(expectedAccommodationDto.getId(), actualDto.getId());
        assertEquals(expectedAccommodationDto.getDailyRate(), actualDto.getDailyRate());
        assertThat(actualDto.getAddress()).usingRecursiveComparison()
                .isEqualTo(expectedAccommodationDto.getAddress());
        assertThat(actualDto.getAmenities()).usingRecursiveComparison()
                .isEqualTo(expectedAccommodationDto.getAmenities());
    }

    @Test
    @DisplayName("Should return a list of available accommodations")
    void getAll_ShouldReturnListOfAccommodations() throws Exception {
        List<AccommodationDto> expectedList = List.of(getExpectedAccommodationDto2());

        MvcResult result = mockMvc.perform(get("/accommodations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson = result.getResponse().getContentAsString();
        List<AccommodationDto> actualList = objectMapper.readValue(actualJson,
                new TypeReference<>() {
                });

        assertEquals(expectedList.size(), actualList.size());
        assertThat(actualList.getFirst()).usingRecursiveComparison()
                .isEqualTo(expectedList.getFirst());
    }

    @Test
    @DisplayName("Should return accommodation details when given a valid ID")
    void getById_ValidId_ShouldReturnAccommodationDto() throws Exception {
        Long id = 1L;
        AccommodationDto expectedDto = getExpectedAccommodationDto2();

        MvcResult result = mockMvc.perform(get("/accommodations/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AccommodationDto actualDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), AccommodationDto.class);

        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getDailyRate(), actualDto.getDailyRate());
        assertThat(actualDto.getAddress()).usingRecursiveComparison()
                .isEqualTo(expectedDto.getAddress());
        assertThat(actualDto.getAmenities()).usingRecursiveComparison()
                .isEqualTo(expectedDto.getAmenities());
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"ADMIN"})
    @DisplayName("Should update accommodation by ID when "
            + "request is valid and user has ADMIN role")
    void update_ValidRequestWithAdminRole_ShouldUpdateAccommodation() throws Exception {
        Long id = 1L;
        AccommodationRequestDto requestDto = getAccommodationUpdateRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        AccommodationDto expectedDto = getAccommodationDtoAfterUpdate();

        MvcResult result = mockMvc.perform(put("/accommodations/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AccommodationDto actualDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), AccommodationDto.class);

        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getDailyRate(), actualDto.getDailyRate());
        assertThat(actualDto.getAddress()).usingRecursiveComparison()
                .isEqualTo(expectedDto.getAddress());
        assertThat(actualDto.getAmenities()).usingRecursiveComparison()
                .isEqualTo(expectedDto.getAmenities());
        assertThat(actualDto.getType()).usingRecursiveComparison()
                .isEqualTo(expectedDto.getType());
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"ADMIN"})
    @DisplayName("Should delete accommodation by ID when user has ADMIN role")
    void deleteById_ExistingIdWithAdminRole_ShouldDeleteAccommodation() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/accommodations/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
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
                    new ClassPathResource(DELETE_ALL_SQL));
        }
    }
}
