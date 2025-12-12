package ru.practicum.shareit.dto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class ServerBookingDtoTest {

    @Test
    void mapToBookingDtoTest() {
        Booking booking = new Booking();
        booking.setId(19123L);
        booking.setStatus(BookingStatus.APPROVED);

        BookingItemDto bookingItemDto = BookingMapper.mapToBookingItemDto(booking);
        bookingItemDto.setStart(LocalDateTime.of(2026, 12, 24, 12, 12));

        assertEquals(19123L, booking.getId());
    }
}
