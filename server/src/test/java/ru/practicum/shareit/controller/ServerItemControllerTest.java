package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(ItemController.class)
public class ServerItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";
    private static final long USER_ID = 1L;

    @Test
    void createItemTest() throws Exception {
        ItemDto requestDto = new ItemDto();
        requestDto.setName("test-item");
        requestDto.setDescription("test-item-desc");
        requestDto.setAvailable(true);
        requestDto.setOwnerId(USER_ID);

        when(itemService.create(USER_ID, requestDto)).thenReturn(requestDto);

        mockMvc.perform(post("/items")
                        .header(CUSTOM_USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(requestDto.getName()));
    }

    @Test
    void updateItemTest() throws Exception {
        Long itemId = 123L;

        ItemDto requestDto = new ItemDto();
        requestDto.setName("test-item");
        requestDto.setDescription("test-item-desc");
        requestDto.setAvailable(true);
        requestDto.setOwnerId(USER_ID);

        when(itemService.create(USER_ID, requestDto)).thenReturn(requestDto);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header(CUSTOM_USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getItemByIdTest() throws Exception {
        Long itemId = 123L;

        ItemDto requestDto = new ItemDto();
        requestDto.setName("test-item");
        requestDto.setDescription("test-item-desc");
        requestDto.setAvailable(true);
        requestDto.setOwnerId(USER_ID);

        when(itemService.create(USER_ID, requestDto)).thenReturn(requestDto);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header(CUSTOM_USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteItemByIdTest() throws Exception {
        Long itemId = 123L;

        ItemDto requestDto = new ItemDto();
        requestDto.setName("test-item");
        requestDto.setDescription("test-item-desc");
        requestDto.setAvailable(true);
        requestDto.setOwnerId(USER_ID);

        when(itemService.create(USER_ID, requestDto)).thenReturn(requestDto);

        mockMvc.perform(delete("/items/{itemId}", itemId)
                        .header(CUSTOM_USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }
}
