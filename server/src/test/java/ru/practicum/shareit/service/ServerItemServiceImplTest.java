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

    @Test
    void createItemTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);

        User user = new User();
        user.setName("test-user");
        user.setEmail("test1@email.com");

        userRepository.save(user);

        ItemDto itemToCreate = itemService.create(user.getId(), itemDto);

        assertNotNull(itemToCreate.getId());
        assertEquals("test-item", itemToCreate.getName());
        assertEquals("test-item-desc", itemToCreate.getDescription());
        assertTrue(itemToCreate.getAvailable());
    }

    @Test
    void updateItemTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);

        User user = new User();
        user.setName("test-user");
        user.setEmail("test1@email.com");

        userRepository.save(user);

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
    void getItemById() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);

        User user = new User();
        user.setName("test-user");
        user.setEmail("test1@email.com");

        userRepository.save(user);

        ItemDto itemDtoToGet = itemService.create(user.getId(), itemDto);

        ItemBookingAndCommentDto itemDtoTest = itemService.getById(itemDtoToGet.getId(), user.getId());

        assertEquals(user.getId(), itemDtoTest.getOwnerId());
    }

    @Test
    void searchTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);

        User user = new User();
        user.setName("test-user");
        user.setEmail("test1@email.com");

        userRepository.save(user);

        itemService.create(user.getId(), itemDto);

        Collection<ItemDto> resultsNegative = itemService.search(1L, "tset");

        assertTrue(resultsNegative.isEmpty());

        Collection<ItemDto> resultsPositive = itemService.search(1L, "test");

        assertFalse(resultsPositive.isEmpty());
    }

    @Test
    void commentTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);

        User user = new User();
        user.setName("test-user");
        user.setEmail("test1@email.com");

        userRepository.save(user);

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

}


