package ru.practicum.shareit.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.BadRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingAndCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repostitory.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
public class ServerItemServiceImplTest {
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

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("test-user");
        user.setEmail("test35@email.com");

        userRepository.save(user);
    }

    @Test
    void createItemTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);
        itemDto.setOwnerId(user.getId());

        ItemDto itemToCreate = itemService.create(user.getId(), itemDto);

        assertNotNull(itemToCreate.getId());
        assertEquals("test-item", itemToCreate.getName());
        assertEquals("test-item-desc", itemToCreate.getDescription());
        assertTrue(itemToCreate.getAvailable());
    }

    @Test
    void createItemWithWrongOwnerTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);
        itemDto.setOwnerId(42L);

        assertThrows(NotFoundException.class, () -> itemService.create(itemDto.getOwnerId(), itemDto));
    }

    @Test
    void updateItemTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);
        itemDto.setOwnerId(user.getId());

        ItemDto itemToCreate = itemService.create(user.getId(), itemDto);

        UpdatedItemDto updatedItemDto = new UpdatedItemDto();
        updatedItemDto.setId(itemToCreate.getId());
        updatedItemDto.setName("test-item-update");
        updatedItemDto.setDescription(itemToCreate.getDescription());
        updatedItemDto.setAvailable(false);

        ItemDto itemToUpdate = itemService.update(updatedItemDto.getId(), user.getId(), updatedItemDto);

        assertNotNull(updatedItemDto.getId());
        assertEquals("test-item-update", itemToUpdate.getName());
        assertEquals("test-item-desc", itemToUpdate.getDescription());
        assertFalse(itemToUpdate.getAvailable());
    }

    @Test
    void updateItemWithWrongOwnerTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);

        ItemDto itemToCreate = itemService.create(user.getId(), itemDto);

        User notOwner = new User();
        notOwner.setName("not-owner");
        notOwner.setEmail("not-owner@email.com");

        userRepository.save(notOwner);

        UpdatedItemDto updatedItemDto = new UpdatedItemDto();
        updatedItemDto.setId(itemToCreate.getId());
        updatedItemDto.setName("test-item-update");
        updatedItemDto.setDescription(itemToCreate.getDescription());
        updatedItemDto.setAvailable(false);


        assertThrows(BadRequest.class, () -> itemService.update(updatedItemDto.getId(), notOwner.getId(), updatedItemDto));
    }

    @Test
    void deleteItemByIdTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);
        itemDto.setOwnerId(user.getId());

        ItemDto itemDtoToGet = itemService.create(user.getId(), itemDto);

        itemService.delete(itemDtoToGet.getId());

        assertThrows(NotFoundException.class, () -> itemService.getById(itemDtoToGet.getId(), user.getId()));
    }

    @Test
    void getItemByIdTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);
        itemDto.setOwnerId(user.getId());

        ItemDto itemDtoToGet = itemService.create(user.getId(), itemDto);

        ItemBookingAndCommentDto itemDtoTest = itemService.getById(itemDtoToGet.getId(), user.getId());

        assertEquals(user.getId(), itemDtoTest.getOwnerId());
    }

    @Test
    void getItemByWrongIdTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);
        itemDto.setOwnerId(user.getId());

        itemService.create(user.getId(), itemDto);

        assertThrows(NotFoundException.class, () -> itemService.getById(42L, user.getId()));
    }

    @Test
    void getItemsByUserIdTest() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("test-item");
        itemDto1.setDescription("test-item-desc");
        itemDto1.setAvailable(true);
        itemDto1.setOwnerId(user.getId());

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("test-item");
        itemDto2.setDescription("test-item-desc");
        itemDto2.setAvailable(true);
        itemDto2.setOwnerId(user.getId());

        itemService.create(user.getId(), itemDto1);
        itemService.create(user.getId(), itemDto2);

        Collection<ItemDto> resultList = itemService.getAll(user.getId());

        assertEquals(2, resultList.size());
    }

    @Test
    void getItemsByWrongUserIdTest() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("test-item");
        itemDto1.setDescription("test-item-desc");
        itemDto1.setAvailable(true);
        itemDto1.setOwnerId(user.getId());

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("test-item");
        itemDto2.setDescription("test-item-desc");
        itemDto2.setAvailable(true);
        itemDto2.setOwnerId(user.getId());

        itemService.create(user.getId(), itemDto1);
        itemService.create(user.getId(), itemDto2);

        assertThrows(NotFoundException.class, () -> itemService.getAll(42L));
    }

    @Test
    void searchSuccessTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);

        itemService.create(user.getId(), itemDto);

        Collection<ItemDto> resultsPositive = itemService.search(1L, "test");

        assertFalse(resultsPositive.isEmpty());
    }

    @Test
    void searchFailTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);

        itemService.create(user.getId(), itemDto);

        Collection<ItemDto> resultsNegative = itemService.search(1L, "tset");

        assertTrue(resultsNegative.isEmpty());
    }

    @Test
    void commentAddSuccessTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);

        ItemDto itemToCreate = itemService.create(user.getId(), itemDto);

        Item item = itemRepository.findById(itemToCreate.getId())
                .orElseThrow(() -> new NotFoundException("вещь не найдена"));

        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now().minusDays(1);

        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setItem(item);

        bookingRepository.save(booking);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("test-comment");

        CommentDto commentToGet = itemService.createComment(itemToCreate.getId(), user.getId(), commentDto.getText());

        assertEquals("test-comment", commentToGet.getText());
    }

    @Test
    void commentAddFailTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);

        ItemDto itemToCreate = itemService.create(user.getId(), itemDto);

        Item item = itemRepository.findById(itemToCreate.getId())
                .orElseThrow(() -> new NotFoundException("вещь не найдена"));

        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setItem(item);

        bookingRepository.save(booking);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("test-comment");

        assertThrows(BadRequest.class, () -> itemService.createComment(itemToCreate.getId(), user.getId(), commentDto.getText()));
    }

}


