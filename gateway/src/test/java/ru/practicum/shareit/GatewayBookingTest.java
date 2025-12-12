package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
public class GatewayBookingTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    @Test
    void getBookingsTest() throws Exception {
        long userId = 1L;
        String stateParam = "ALL";
        int from = 0;
        int size = 10;

        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.getBookings(userId, Objects.requireNonNull(BookingState.from(stateParam).orElse(null)), from, size))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", stateParam)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingTest() throws Exception {
        long userId = 1L;
        Long bookingId = 1L;

        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.getBooking(userId, bookingId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void createBookingTest() throws Exception {
        long userId = 1L;
        BookItemRequestDto requestDto = new BookItemRequestDto();
        requestDto.setItemId(1L);
        requestDto.setStart(LocalDateTime.of(2026, 1, 12, 12, 10));
        requestDto.setEnd(LocalDateTime.of(2026, 1, 12, 12, 10));

        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.create(userId, requestDto)).thenReturn(expectedResponse);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateBookingTest() throws Exception {
        long userId = 1L;
        Long bookingId = 1L;
        BookItemRequestDto requestDto = new BookItemRequestDto();
        requestDto.setStart(LocalDateTime.of(2026, 1, 12, 12, 10));
        requestDto.setEnd(LocalDateTime.of(2026, 1, 12, 12, 10));

        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.update(bookingId, userId, true)).thenReturn(expectedResponse);

        mockMvc.perform(patch("/bookings/{bookingId}?approved=true", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

}

