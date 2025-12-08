package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime start, LocalDateTime end);

    Collection<Booking> findByBookerIdAndEndBefore(Long bookerId, LocalDateTime end);

    Collection<Booking> findByBookerIdAndStartAfter(Long bookerId, LocalDateTime start);

    Collection<Booking> findByBookerIdAndStatus(Long bookerId, BookingState bookingState);

    Collection<Booking> findByBookerId(Long bookerId);

    Collection<Booking> findByItemOwnerIdAndStartBeforeAndEndAfter(Long ownerId, LocalDateTime start, LocalDateTime end);

    Collection<Booking> findByItemOwnerIdAndEndBefore(Long ownerId, LocalDateTime end);

    Collection<Booking> findByItemOwnerIdAndStartAfter(Long ownerId, LocalDateTime start);

    Collection<Booking> findByItemOwnerIdAndStatus(Long ownerId, BookingState bookingState);

    Collection<Booking> findByItemOwnerId(Long ownerId);

    Optional<Booking> findFirstByItemIdAndBookerIdAndStatusAndEndBefore(Long itemId, Long userId,
                                                                        BookingStatus bookingStatus,
                                                                        LocalDateTime endDate);

    List<Booking> findByItemIdAndEndBefore(Long id, LocalDateTime end);

    List<Booking> findByItemIdAndStartAfter(Long id, LocalDateTime start);
}
