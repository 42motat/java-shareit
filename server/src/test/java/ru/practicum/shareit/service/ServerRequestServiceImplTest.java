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
        itemRequestRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

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

        requestor.setId(420000L);

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

//    @Test
//    void getItemRequestsOfUserTest() {
//        ItemRequestDto requestDto1 = new ItemRequestDto();
//        requestDto1.setDescription("test-request");
//        requestDto1.setRequestor(requestor);
//        requestDto1.setCreated(LocalDateTime.now().plusDays(1));
//
//        ItemRequestDto requestDto2 = new ItemRequestDto();
//        requestDto2.setDescription("test-request");
//        requestDto2.setRequestor(requestor);
//        requestDto2.setCreated(LocalDateTime.now().plusDays(2));
//
//        itemRequestService.create(requestor.getId(), requestDto1);
//        itemRequestService.create(requestor.getId(), requestDto2);
//
//        assertEquals(2, itemRequestService.getAllItemRequestsOfUser(requestor.getId()).size());
//    }

    @Test
    void getItemRequestsOfUnknownUserTest() {
        ItemRequestDto requestDto1 = new ItemRequestDto();
        requestDto1.setDescription("test-request");
        requestDto1.setCreated(LocalDateTime.now().plusDays(1));

        ItemRequestDto requestDto2 = new ItemRequestDto();
        requestDto2.setDescription("test-request");
        requestDto2.setCreated(LocalDateTime.now().plusDays(2));

        itemRequestService.create(requestor.getId(), requestDto1);
        itemRequestService.create(requestor.getId(), requestDto2);

        assertThrows(NotFoundException.class, () -> itemRequestService.getAllItemRequestsOfUser(requestor.getId() + 4200));
    }

    @Test
    void getItemRequestsOfUserTest() {
        ItemRequestDto requestDto1 = new ItemRequestDto();
        requestDto1.setDescription("test-request");
        requestDto1.setRequestor(requestor);
        requestDto1.setCreated(LocalDateTime.now().plusDays(1));

        ItemRequestDto requestDto2 = new ItemRequestDto();
        requestDto2.setDescription("test-request");
        requestDto2.setRequestor(requestor);
        requestDto2.setCreated(LocalDateTime.now().plusDays(2));

        itemRequestService.create(requestor.getId(), requestDto1);
        itemRequestService.create(requestor.getId(), requestDto2);

        User anotherRequestor = new User();
        requestor.setName("test-another-user");
        requestor.setEmail("test17000@email.com");

        userRepository.save(anotherRequestor);

        ItemRequestDto requestDto3 = new ItemRequestDto();
        requestDto3.setDescription("test-request");
        requestDto3.setRequestor(anotherRequestor);
        requestDto3.setCreated(LocalDateTime.now().plusDays(1));

        ItemRequestDto requestDto4 = new ItemRequestDto();
        requestDto4.setDescription("test-request");
        requestDto4.setRequestor(anotherRequestor);
        requestDto4.setCreated(LocalDateTime.now().plusDays(2));

        itemRequestService.create(anotherRequestor.getId(), requestDto3);
        itemRequestService.create(anotherRequestor.getId(), requestDto4);


        assertEquals(2, itemRequestService.getAllItemRequestsOfUser(requestor.getId()).size());
    }
}