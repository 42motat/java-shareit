package ru.practicum.shareit.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.BadRequest;
import ru.practicum.shareit.exception.Forbidden;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repostitory.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
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

    private User booker;
    private User owner;
    private Item item;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        bookingRepository.deleteAll();

        owner = new User();
        owner.setName("test-owner");
        owner.setEmail("test220@email.com");

        userRepository.save(owner);

        item = new Item();
        item.setName("test-item");
        item.setDescription("test-item-desc");
        item.setAvailable(Boolean.TRUE);
        item.setOwnerId(owner.getId());

        itemRepository.save(item);

        booker = new User();
        booker.setName("test-booker");
        booker.setEmail("test240@email.com");

        userRepository.save(booker);
    }

    @Test
    void createBookingTest() {
        NewBookingDto bookingDto = new NewBookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.of(2026, 12, 10, 11, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2026, 12, 10, 12, 0, 0));
        bookingDto.setBookerId(booker.getId());

        BookingDto bookingToCreate = bookingService.create(booker.getId(), bookingDto);

        assertNotNull(bookingToCreate.getId());
        assertEquals(item.getId(), bookingToCreate.getItem().getId());
        assertEquals("2026-12-10T11:00", bookingToCreate.getStart().toString());
        assertEquals("2026-12-10T12:00", bookingToCreate.getEnd().toString());
        assertEquals(booker.getId(), bookingToCreate.getBooker().getId());
    }

    @Test
    void createBookingBookerNotFoundTest() {
        NewBookingDto bookingDto = new NewBookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.of(1986, 4, 26, 1, 23, 0));
        bookingDto.setEnd(LocalDateTime.of(2026, 12, 10, 12, 0, 0));
        bookingDto.setBookerId(booker.getId());

        assertThrows(NotFoundException.class, () -> bookingService.create(4200L, bookingDto));
    }

    @Test
    void createBookingItemNotFoundTest() {
        NewBookingDto bookingDto = new NewBookingDto();
        bookingDto.setItemId(42L);
        bookingDto.setStart(LocalDateTime.of(1986, 4, 26, 1, 23, 0));
        bookingDto.setEnd(LocalDateTime.of(2026, 12, 10, 12, 0, 0));
        bookingDto.setBookerId(booker.getId());

        assertThrows(NotFoundException.class, () -> bookingService.create(bookingDto.getBookerId(), bookingDto));
    }

    @Test
    void createBookingItemNotAvailableTest() {
        Item anotherItem = new Item();
        anotherItem.setName("test-item-unavailable");
        anotherItem.setDescription("test-item-desc");
        anotherItem.setAvailable(Boolean.FALSE);
        anotherItem.setOwnerId(owner.getId());

        itemRepository.save(anotherItem);

        NewBookingDto bookingDto = new NewBookingDto();
        bookingDto.setItemId(anotherItem.getId());
        bookingDto.setStart(LocalDateTime.of(1986, 4, 26, 1, 23, 0));
        bookingDto.setEnd(LocalDateTime.of(2026, 12, 10, 12, 0, 0));
        bookingDto.setBookerId(booker.getId());

        assertThrows(BadRequest.class, () -> bookingService.create(bookingDto.getBookerId(), bookingDto));
    }

    @Test
    void updateBookingTest() {
        NewBookingDto bookingDto = new NewBookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.of(2026, 12, 10, 11, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2026, 12, 10, 12, 0,0));
        bookingDto.setBookerId(booker.getId());

        BookingDto bookingToCreate = bookingService.create(booker.getId(), bookingDto);

        bookingToCreate = bookingService.updateBookingStatus(owner.getId(), bookingToCreate.getId(), true);

        assertEquals("APPROVED", bookingToCreate.getStatus().toString());
    }

    @Test
    void getBookingByIdTest() {
        NewBookingDto bookingDto = new NewBookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.of(2026, 12, 10, 11, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2026, 12, 10, 12, 0, 0));
        bookingDto.setBookerId(booker.getId());

        BookingDto bookingToCreate = bookingService.create(booker.getId(), bookingDto);

        assertDoesNotThrow(() -> bookingService.getById(booker.getId(), bookingToCreate.getId()));
    }

    @Test
    void getBookingBySomeOtherUserTest() {
        NewBookingDto bookingDto = new NewBookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.of(2026, 12, 10, 11, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2026, 12, 10, 12, 0, 0));
        bookingDto.setBookerId(booker.getId());

        BookingDto bookingToCreate = bookingService.create(booker.getId(), bookingDto);

        User someOtherUser = new User();
        someOtherUser.setName("test-booker");
        someOtherUser.setEmail("test0240@email.com");

        userRepository.save(someOtherUser);

        assertThrows(Forbidden.class, () -> bookingService.getById(someOtherUser.getId(), bookingToCreate.getId()));
    }

    @Test
    void getCurrentBookingsByBookerTest() {
        NewBookingDto bookingDto = new NewBookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.of(2026, 12, 10, 11, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2026, 12, 10, 12, 0, 0));
        bookingDto.setBookerId(booker.getId());

        BookingDto bookingToCreate = bookingService.create(booker.getId(), bookingDto);

        assertEquals(0, bookingService.getAllBookingsByBooker(booker.getId(), BookingState.CURRENT).size());
    }

    @Test
    void getFutureBookingsByBookerTest() {
        NewBookingDto bookingDto = new NewBookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.of(2026, 12, 10, 11, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2026, 12, 10, 12, 0, 0));
        bookingDto.setBookerId(booker.getId());

        BookingDto bookingToCreate = bookingService.create(booker.getId(), bookingDto);

        BookingDto updatedBookingDtoToCreate = bookingService.updateBookingStatus(owner.getId(),
                bookingToCreate.getId(),
                false);

        assertEquals(1, bookingService.getAllBookingsByBooker(booker.getId(), BookingState.FUTURE).size());
    }

    @Test
    void getCurrentBookingsByOwnerTest() {
        NewBookingDto bookingDto = new NewBookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.of(2026, 12, 10, 11, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2026, 12, 10, 12, 0, 0));
        bookingDto.setBookerId(booker.getId());

        BookingDto bookingToCreate = bookingService.create(booker.getId(), bookingDto);

        assertEquals(0, bookingService.getAllBookingsOfOwner(owner.getId(), BookingState.CURRENT).size());
    }

}
