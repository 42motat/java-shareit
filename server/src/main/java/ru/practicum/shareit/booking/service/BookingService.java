package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {
    BookingDto getById(Long userId, Long bookingId);

    Collection<BookingDto> getAllBookingsOfOwner(Long ownerId, BookingState bookingState);

    Collection<BookingDto> getAllBookingsByBooker(Long bookerId, BookingState bookingState);

    BookingDto create(Long userId, NewBookingDto newBookingDto);

    BookingDto updateBookingStatus(Long userId, Long bookingId, Boolean approved);


}
