package ru.practicum.shareit.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
public class ServerRequestServiceImplTest {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void createItemRequestTest() {
        UserDto userDto = new UserDto();
        userDto.setName("test-user");
        userDto.setEmail("test17@email.com");

        UserDto userToCreate = userService.create(userDto);

        User requestor = userRepository.findById(userToCreate.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("test-request");
        requestDto.setRequestor(requestor);
        requestDto.setCreated(LocalDateTime.now());

        ItemRequestDto requestToCreate = itemRequestService.create(requestor.getId(), requestDto);

        assertNotNull(requestToCreate.getId());
        assertEquals("test-request", requestDto.getDescription());
        assertEquals(requestor.getId(), requestToCreate.getRequestor().getId());
    }
}
