package ru.practicum.shareit.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@WebMvcTest(UserController.class)
class ErrorHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto();
        userDto.setName("test-user-dto");
        userDto.setEmail("test666@email.com");

        userService.create(userDto);
    }

    @Test
    void handleBadRequestTest() throws Exception {
        UserDto requestDto = new UserDto();
        requestDto.setName("");
        requestDto.setEmail("test888@email.com");

        when(userService.create(requestDto)).thenThrow(BadRequest.class);

        mockMvc.perform(post("/users")
                        .header(CUSTOM_USER_ID_HEADER, requestDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void handleNotFoundExceptionTest() throws Exception {
        when(userService.getById(anyLong())).thenThrow(new NotFoundException("Пользователь не найден"));

        mockMvc.perform(get("/users/666")
                        .header(CUSTOM_USER_ID_HEADER, 666L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Пользователь не найден"));
    }

    @Test
    void handleConflictTest() throws Exception {
        UserDto requestDto = new UserDto();
        requestDto.setName("test-user");
        requestDto.setEmail("test666@email.com");

        when(userService.create(requestDto)).thenThrow(Conflict.class);

        mockMvc.perform(post("/users")
                        .header(CUSTOM_USER_ID_HEADER, requestDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void handleInternalServerErrorTest() throws Exception {
        UserDto requestDto = new UserDto();
        requestDto.setName("test-user");
        requestDto.setEmail("test666@email.com");

        when(userService.create(requestDto)).thenThrow(new RuntimeException("Ошибка сервера"));

        mockMvc.perform(post("/users")
                        .header(CUSTOM_USER_ID_HEADER, requestDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }
}
