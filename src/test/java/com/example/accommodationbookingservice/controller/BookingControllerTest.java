package com.example.accommodationbookingservice.controller;

import static com.example.accommodationbookingservice.testutil.BookingUtil.getBookingDto2;
import static com.example.accommodationbookingservice.testutil.BookingUtil.getBookingRequestDto2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.accommodationbookingservice.dto.bookingdto.BookingDto;
import com.example.accommodationbookingservice.dto.bookingdto.BookingRequestDto;
import com.example.accommodationbookingservice.testutil.BookingUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
class BookingControllerTest {

    private static final String DELETE_ALL_SQL = "database/delete/delete-all.sql";
    private static final String INSERT_ACCOMMODATION_SQL =
            "database/accommodation/insert-accommodation.sql";
    private static final String BOOKING_INSERT_BOOKING_SQL = "database/booking/insert-booking.sql";
    private static final Long ID = 1L;
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
    @WithMockUser(username = "customer@example.com", authorities = {"CUSTOMER"})
    @DisplayName("Should create a new booking when request is valid and user has CUSTOMER role")
    void save_ValidBookingWithCustomerRole_ShouldCreateBooking() throws Exception {
        BookingRequestDto bookingRequestDto = getBookingRequestDto2();
        String jsonRequest = objectMapper.writeValueAsString(bookingRequestDto);
        BookingDto expected = getBookingDto2();

        MvcResult result = mockMvc.perform(post("/bookings")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookingDto actualBooking = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookingDto.class);
        expected.setCreateAt(actualBooking.getCreateAt());

        assertThat(actualBooking).usingRecursiveComparison()
                .ignoringFields("roles")
                .isEqualTo(expected);
        assertThat(actualBooking.getUser().getRoles())
                .usingRecursiveComparison().isEqualTo(expected.getUser().getRoles());
    }

    @Test
    @WithMockUser(username = "customer@example.com", authorities = {"CUSTOMER"})
    @DisplayName("Should return list of bookings for currently authenticated user")
    void getUserBookings_ValidUser_ShouldReturnBookings() throws Exception {
        String name = "John";
        BookingDto expectedBooking1 = BookingUtil.getExpectedBookingDto();
        BookingDto expectedBooking2 = BookingUtil.getExpectedBookingDto2();

        MvcResult result = mockMvc.perform(get("/bookings/my", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson = result.getResponse().getContentAsString();
        List<BookingDto> actualList = objectMapper.readValue(actualJson,
                new TypeReference<>() {
                });
        BookingDto actualFirstBooking = actualList.getFirst();
        BookingDto actualSecondBooking = actualList.get(1);

        expectedBooking1.setCreateAt(actualFirstBooking.getCreateAt());
        expectedBooking2.setCreateAt(actualSecondBooking.getCreateAt());

        assertThat(actualFirstBooking).usingRecursiveComparison()
                .isEqualTo(expectedBooking1);
        assertThat(actualSecondBooking).usingRecursiveComparison()
                .isEqualTo(expectedBooking2);
    }

    @Test
    @WithMockUser(username = "customer@example.com", authorities = {"CUSTOMER"})
    @DisplayName("Should return booking by ID if it belongs to authenticated user")
    void getBookingById_ValidIdAndOwner_ShouldReturnBooking() throws Exception {
        BookingDto expectedBooking = BookingUtil.getExpectedBookingDto();

        MvcResult result = mockMvc.perform(get("/bookings/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookingDto actualBookingDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookingDto.class);
        expectedBooking.setCreateAt(actualBookingDto.getCreateAt());

        assertThat(actualBookingDto).usingRecursiveComparison()
                .isEqualTo(expectedBooking);
    }

    @Test
    @WithMockUser(username = "customer@example.com", authorities = {"ADMIN"})
    @DisplayName("Should return all bookings when user has ADMIN authority")
    void getAllBookings_AsAdmin_ShouldReturnAllBookings() throws Exception {
        BookingDto expectedBooking1 = BookingUtil.getExpectedBookingDto();
        BookingDto expectedBooking2 = BookingUtil.getExpectedBookingDto2();

        MvcResult result = mockMvc.perform(get("/bookings/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson = result.getResponse().getContentAsString();
        List<BookingDto> actualList = objectMapper.readValue(actualJson,
                new TypeReference<>() {
                });

        BookingDto actualFirstBooking = actualList.getFirst();
        BookingDto actualSecondBooking = actualList.get(1);

        expectedBooking1.setCreateAt(actualFirstBooking.getCreateAt());
        expectedBooking2.setCreateAt(actualSecondBooking.getCreateAt());

        assertThat(actualFirstBooking).usingRecursiveComparison()
                .isEqualTo(expectedBooking1);
        assertThat(actualSecondBooking).usingRecursiveComparison()
                .isEqualTo(expectedBooking2);
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"ADMIN"})
    @DisplayName("Should return bookings by user ID when user has ADMIN authority")
    void getBookingsByUserId_AsAdmin_ShouldReturnUserBookings() throws Exception {
        // given
        BookingDto expectedBooking1 = BookingUtil.getExpectedBookingDto();
        BookingDto expectedBooking2 = BookingUtil.getExpectedBookingDto2();

        MvcResult result = mockMvc.perform(get("/bookings/{id}/user", ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookingDto> actualList = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });

        BookingDto actualFirstBooking = actualList.getFirst();
        BookingDto actualSecondBooking = actualList.get(1);

        expectedBooking1.setCreateAt(actualFirstBooking.getCreateAt());
        expectedBooking2.setCreateAt(actualSecondBooking.getCreateAt());

        assertThat(actualFirstBooking).usingRecursiveComparison()
                .isEqualTo(expectedBooking1);
        assertThat(actualSecondBooking).usingRecursiveComparison()
                .isEqualTo(expectedBooking2);
    }

    @Test
    @WithMockUser(username = "customer@example.com", authorities = {"CUSTOMER"})
    @DisplayName("Should update booking when request is valid and user is owner")
    void updateBooking_ValidRequestAndOwner_ShouldUpdateBooking() throws Exception {
        BookingRequestDto requestDto = BookingUtil.getBookingRequestDto3();
        BookingDto expectedBooking = BookingUtil.getExpectedBookingDto3();

        String request = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/bookings/{id}", ID)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookingDto actualBookingDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookingDto.class);
        expectedBooking.setCreateAt(actualBookingDto.getCreateAt());

        assertThat(actualBookingDto).usingRecursiveComparison()
                .isEqualTo(expectedBooking);
    }

    @Test
    @WithMockUser(username = "customer@example.com", authorities = {"CUSTOMER"})
    @DisplayName("Should cancel booking when status is PENDING and user is owner")
    void cancelBooking_ValidBookingWithPendingStatus_ShouldCancelBooking() throws Exception {
        BookingDto expectedBooking = BookingUtil.getExpectedBookingDto4();

        MvcResult result = mockMvc.perform(delete("/bookings/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookingDto actualBookingDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookingDto.class);
        expectedBooking.setCreateAt(actualBookingDto.getCreateAt());

        assertThat(actualBookingDto).usingRecursiveComparison()
                .isEqualTo(expectedBooking);
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
                    new ClassPathResource(DELETE_ALL_SQL));
        }
    }
}
