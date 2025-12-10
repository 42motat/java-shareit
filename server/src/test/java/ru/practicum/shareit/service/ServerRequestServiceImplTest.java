package ru.practicum.shareit.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repostitory.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
public class ServerRequestServiceImplTest {
    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User requestor;
    private Item item;

    @BeforeEach
    void setUp() {
        requestor = new User();
        requestor.setName("test-user");
        requestor.setEmail("test17@email.com");

        userRepository.save(requestor);

    }

    @Test
    void createItemRequestTest() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("test-request");
        requestDto.setRequestor(requestor);
        requestDto.setCreated(LocalDateTime.now());

        ItemRequestDto requestToCreate = itemRequestService.create(requestor.getId(), requestDto);

        assertNotNull(requestToCreate.getId());
        assertEquals("test-request", requestDto.getDescription());
        assertEquals(requestor.getId(), requestToCreate.getRequestor().getId());
    }

    @Test
    void createItemRequestUserNotFoundTest() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("test-request");
        requestDto.setRequestor(requestor);
        requestDto.setCreated(LocalDateTime.now());

        requestor.setId(42L);

        assertThrows(NotFoundException.class, () -> itemRequestService.create(requestor.getId(), requestDto));
    }

    @Test
    void getItemRequestTest() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("test-request");
        requestDto.setRequestor(requestor);
        requestDto.setCreated(LocalDateTime.now());

        ItemRequestDto requestToCreate = itemRequestService.create(requestor.getId(), requestDto);

        assertEquals(requestToCreate.getId(), itemRequestService.getById(requestor.getId(), requestToCreate.getId()).getId());
    }

    @Test
    void getItemRequestFailTest() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("test-request");
        requestDto.setRequestor(requestor);
        requestDto.setCreated(LocalDateTime.now());

        ItemRequestDto requestToCreate = itemRequestService.create(requestor.getId(), requestDto);

        assertEquals(requestToCreate.getId(), itemRequestService.getById(requestor.getId(), requestToCreate.getId()).getId());
    }
}