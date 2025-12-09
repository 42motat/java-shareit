package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class ServerUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    void createUserTest() throws Exception {
        UserDto requestDto = new UserDto();
        requestDto.setName("test-user");
        requestDto.setEmail("test1@email.com");

        when(userService.create(requestDto)).thenReturn(requestDto);

        mockMvc.perform(post("/users")
                .header(CUSTOM_USER_ID_HEADER, requestDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(requestDto.getName()));
    }

    @Test
    void updateUserTest() throws Exception {
        Long userId = 1L;

        UserDto requestDto = new UserDto();
        requestDto.setName("test-user");
        requestDto.setEmail("test2@email.com");

        when(userService.create(requestDto)).thenReturn(requestDto);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .header(CUSTOM_USER_ID_HEADER, requestDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

}
