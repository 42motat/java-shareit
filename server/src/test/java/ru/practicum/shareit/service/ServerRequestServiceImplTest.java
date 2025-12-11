package ru.practicum.shareit.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repostitory.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

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
    private User owner;
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

        owner = new User();
        owner.setName("test-owmer");
        owner.setEmail("test1976@email.com");

        userRepository.save(owner);
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
    void createItemRequestWithItemsTest() {
        ItemForItemRequestDto item1 = new ItemForItemRequestDto();
        item1.setName("test-item-1");
        item1.setDescription("test-item-1-desc");
        item1.setAvailable(true);
        item1.setOwnerId(owner.getId());

        ItemRequestWithItemsDto requestDto = new ItemRequestWithItemsDto();
        requestDto.setDescription("test-request-with-items");
        requestDto.setRequestorId(requestor.getId());
        requestDto.setCreated(LocalDateTime.now());
        requestDto.setItems(List.of(item1));

        assertEquals(1, requestDto.getItems().size());
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
        requestor.setEmail("test17001@email.com");

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

    @Test
    void getItemRequestsOfOtherUserTest() {
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
        anotherRequestor.setName("test-another-user");
        anotherRequestor.setEmail("test17100@email.com");

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


        assertEquals(2, itemRequestService.getAllRequestsOfOtherUsers(requestor.getId()).size());
    }

    @Test
    void createDirectlyIntoRepoTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("direct-insert-test");
        itemRequest.setRequestor(requestor);

        assertDoesNotThrow(() -> itemRequestRepository.save(itemRequest));
    }

    @Test
    void getByIdDirectlyFromRepoTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("direct-insert-test");
        itemRequest.setRequestor(requestor);

        itemRequestRepository.save(itemRequest);

        assertDoesNotThrow(() -> itemRequestRepository.findById(itemRequest.getId()));
    }

    @Test
    void getAllItemRequestOfUserDirectlyFromRepoTest() {
        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setDescription("direct-insert-test");
        itemRequest1.setRequestor(requestor);

        itemRequestRepository.save(itemRequest1);

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setDescription("direct-insert-test");
        itemRequest2.setRequestor(requestor);

        itemRequestRepository.save(itemRequest2);

        assertDoesNotThrow(() -> itemRequestRepository.findAllByRequestorId(requestor.getId()));
    }
}