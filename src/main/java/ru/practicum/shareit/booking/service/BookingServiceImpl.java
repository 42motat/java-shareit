package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequest;
import ru.practicum.shareit.exception.Forbidden;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repostitory.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwnerId().equals(userId)) {
            throw new Forbidden("Недостаточно прав на просмотр");
        }

        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> getAllBookingsByBooker(Long bookerId, BookingState bookingState) {
        userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> bookings;

        switch (bookingState) {
            case CURRENT -> bookings = bookingRepository
                            .findByBookerIdAndStartBeforeAndEndAfter(bookerId, now, now);
            case PAST -> bookings = bookingRepository
                            .findByBookerIdAndEndBefore(bookerId, now);
            case FUTURE -> bookings = bookingRepository
                            .findByBookerIdAndStartAfter(bookerId, now);
            case WAITING -> bookings = bookingRepository
                            .findByBookerIdAndStatus(bookerId, BookingState.WAITING);
            case REJECTED -> bookings = bookingRepository
                            .findByBookerIdAndStatus(bookerId, BookingState.REJECTED);
            default -> bookings = bookingRepository
                            .findByBookerId(bookerId);
        }

        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getAllBookingsOfOwner(Long ownerId, BookingState bookingState) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> bookings;

        switch (bookingState) {
            case CURRENT -> bookings = bookingRepository
                            .findByItemOwnerIdAndStartBeforeAndEndAfter(ownerId, now, now);
//                            .findByItemOwnerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateAsc(ownerId, now, now);
            case PAST -> bookings = bookingRepository
                            .findByItemOwnerIdAndEndBefore(ownerId, now);
//                            .findByItemOwnerIdAndEndDateBeforeOrderByEndDateAsc(ownerId, now);
            case FUTURE -> bookings = bookingRepository
                            .findByItemOwnerIdAndStartAfter(ownerId, now);
//                            .findByItemOwnerIdAndStartDateAfterOrderByStartDateAsc(ownerId, now);
            case WAITING -> bookings = bookingRepository
                            .findByItemOwnerIdAndStatus(ownerId, BookingState.WAITING);
//                            .findByItemOwnerIdAndStatusOrderByStartDateAsc(ownerId, BookingState.WAITING);
            case REJECTED -> bookings = bookingRepository
                            .findByItemOwnerIdAndStatus(ownerId, BookingState.REJECTED);
//                            .findByItemOwnerIdAndStatusOrderByStartDateAsc(ownerId, BookingState.REJECTED);
            default -> bookings = bookingRepository
//                            .findByItemOwnerIdOrderByStartDateAsc(ownerId);
                            .findByItemOwnerId(ownerId);
        }

        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    @Override
    public BookingDto create(Long userId, NewBookingDto newBookingDto) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(newBookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Указанная вещь не найдена"));

        if (!item.getAvailable()) {
            throw new BadRequest("Вещь недоступна для бронирования");
        }

        Booking booking = BookingMapper.mapToBooking(booker, item, newBookingDto);
        booking.setStatus(BookingStatus.WAITING);

        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getItem().getOwnerId().equals(userId)) {
            throw new Forbidden("Только владелец может подтвердить бронирование");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new BadRequest("Статус отличается от 'Ожидает подтверждения'");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }
}
