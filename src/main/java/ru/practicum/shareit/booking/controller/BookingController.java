package ru.practicum.shareit.booking.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader(CUSTOM_USER_ID_HEADER) @Positive Long userId,
                             @RequestBody NewBookingDto newBookingDto) {
        return bookingService.create(userId, newBookingDto);
    }

    @PatchMapping("/{id}")
    public BookingDto updateStatus(@RequestHeader(CUSTOM_USER_ID_HEADER) @Positive Long userId,
                                   @PathVariable Long id,
                                   @RequestParam Boolean approved) {
        return bookingService.updateBookingStatus(userId, id, approved);
    }

    @GetMapping("/{id}")
    public BookingDto getById(@RequestHeader(CUSTOM_USER_ID_HEADER) @Positive Long userId,
                              @PathVariable Long id) {
        return bookingService.getById(userId, id);
    }

    @GetMapping
    public Collection<BookingDto> getBookingsForBooker(@RequestHeader(CUSTOM_USER_ID_HEADER) @Positive Long userId,
                                                     @RequestParam(defaultValue = "ALL") BookingState bookingState) {
        return bookingService.getAllBookingsByBooker(userId, bookingState);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getBookingsForOwner(@RequestHeader(CUSTOM_USER_ID_HEADER) @Positive Long userId,
                                                     @RequestParam(defaultValue = "ALL") BookingState bookingState) {
        return bookingService.getAllBookingsOfOwner(userId, bookingState);
    }

}
