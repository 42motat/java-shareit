package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.dto.ItemBookingAndCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.repostitory.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
public class ServerItemServiceImplTest {
    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

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
        user.setEmail("test2@email.com");

        userRepository.save(user);

        ItemDto itemToCreate = itemService.create(user.getId(), itemDto);

        UpdatedItemDto updatedItemDto = new UpdatedItemDto();
        updatedItemDto.setName("test-item-update");
        updatedItemDto.setAvailable(false);

        ItemDto itemToUpdate = itemService.update(user.getId(), itemToCreate.getId(), updatedItemDto);

        assertNotNull(itemToCreate.getId());
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
        user.setEmail("test3@email.com");

        userRepository.save(user);

        itemService.create(user.getId(), itemDto);

        ItemBookingAndCommentDto itemDtoTest = itemService.getById(1L, 1L);

        assertEquals(1L, itemDtoTest.getOwnerId());
    }

    @Test
    void searchTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test-item");
        itemDto.setDescription("test-item-desc");
        itemDto.setAvailable(true);

        User user = new User();
        user.setName("test-user");
        user.setEmail("test3@email.com");

        userRepository.save(user);

        itemService.create(user.getId(), itemDto);

        Collection<ItemDto> resultsNegative = itemService.search(1L, "tset");

        assertTrue(resultsNegative.isEmpty());

        Collection<ItemDto> resultsPositive = itemService.search(1L, "test");

        assertFalse(resultsPositive.isEmpty());
    }

}


