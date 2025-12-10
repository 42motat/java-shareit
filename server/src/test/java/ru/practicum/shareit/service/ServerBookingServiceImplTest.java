package ru.practicum.shareit.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repostitory.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
public class ServerBookingServiceImplTest {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void createBookingTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);

        UserDto userDto = new UserDto();
        userDto.setName("test-user");
        userDto.setEmail("test22@email.com");

        UserDto userToCreate = userService.create(userDto);

        ItemDto itemToCreate = itemService.create(userToCreate.getId(), itemDto);

        Item item = itemRepository.findById(itemToCreate.getId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        User booker = userRepository.findById(userToCreate.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        NewBookingDto bookingDto = new NewBookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.of(2026, 12, 10, 11, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2026, 12, 10, 12, 0, 0));
        bookingDto.setBookerId(booker.getId());
        bookingDto.setStatus(BookingStatus.WAITING);

        BookingDto bookingToCreate = bookingService.create(booker.getId(), bookingDto);

        assertNotNull(bookingToCreate.getId());
        assertEquals(item.getId(), bookingToCreate.getItem().getId());
        assertEquals("2026-12-10T11:00", bookingToCreate.getStart().toString());
        assertEquals("2026-12-10T12:00", bookingToCreate.getEnd().toString());
        assertEquals(booker.getId(), bookingToCreate.getBooker().getId());
    }

    @Test
    void updateBookingTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);

        UserDto userDto = new UserDto();
        userDto.setName("test-user");
        userDto.setEmail("test21@email.com");

        UserDto userToCreate = userService.create(userDto);

        ItemDto itemToCreate = itemService.create(userToCreate.getId(), itemDto);

        Item item = itemRepository.findById(itemToCreate.getId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        User booker = userRepository.findById(userToCreate.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        NewBookingDto bookingDto = new NewBookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.of(2026, 12, 10, 11, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2026, 12, 10, 12, 0,0));
        bookingDto.setBookerId(booker.getId());
        bookingDto.setStatus(BookingStatus.WAITING);

        BookingDto bookingToCreate = bookingService.create(booker.getId(), bookingDto);

        NewBookingDto updatedBookingDto = new NewBookingDto();
        updatedBookingDto.setStatus(BookingStatus.APPROVED);

        BookingDto updatedBookingDtoToCreate = bookingService.updateBookingStatus(booker.getId(),
                                                                                  bookingToCreate.getId(),
                                                                         true);

        assertEquals(BookingStatus.APPROVED, updatedBookingDtoToCreate.getStatus());
    }



}
