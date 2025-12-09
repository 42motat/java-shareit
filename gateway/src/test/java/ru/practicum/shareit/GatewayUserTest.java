package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class GatewayUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    private final String HEADER = "X-Sharer-User-Id";

    @Test
    void getUserByIdTest() throws Exception {
        long userId = 1L;

        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(userClient.getById(userId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/users/{userId}", userId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void createUserTest() throws Exception {
        long userId = 1L;
        UserDto requestDto = new UserDto();
        requestDto.setName("test-item");
        requestDto.setEmail("email@email.com");

        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(userClient.create(requestDto)).thenReturn(expectedResponse);

        mockMvc.perform(post("/users")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUserTest() throws Exception {
        long userId = 1L;
        UpdateUserDto requestDto = new UpdateUserDto();
        requestDto.setName("test-item");
        requestDto.setEmail("email@email.com");

        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(userClient.update(userId, requestDto)).thenReturn(expectedResponse);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

}

