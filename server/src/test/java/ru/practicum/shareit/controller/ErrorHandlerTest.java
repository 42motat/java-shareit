package ru.practicum.shareit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import lombok.extern.slf4j.Slf4j;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@WebMvcTest(ErrorHandler.class)
class ErrorHandlerTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ErrorHandler errorHandler;

    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setName("test-user-dto");
        userDto.setEmail("test666@email.com");

        userService.create(userDto);
    }

    @Test
    void handleBadRequestTest() {
        BadRequest badRequest = new BadRequest("Bad Request Error");

        errorHandler.handleBadRequestException(badRequest);

        assertEquals("Bad Request Error", badRequest.getMessage());
    }

    @Test
    void handleConflictTest() {
        Conflict conflict = new Conflict("Conflict");

        errorHandler.handleConflictException(conflict);

        assertEquals("Conflict", conflict.getMessage());
    }

    @Test
    void handleForbiddenTest() {
        Forbidden forbidden = new Forbidden("Forbidden");

        errorHandler.handleForbiddenException(forbidden);

        assertEquals("Forbidden", forbidden.getMessage());
    }

    @Test
    void handleNotFoundExceptionTest() {
        NotFoundException notFoundException = new NotFoundException("Not Found");

        errorHandler.handleNotFoundException(notFoundException);

        assertEquals("Not Found", notFoundException.getMessage());
    }
}