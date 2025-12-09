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
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class GatewayItemTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;

    private final String HEADER = "X-Sharer-User-Id";

    @Test
    void getItemByIdTest() throws Exception {
        long userId = 1L;
        Long itemId = 1L;

        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.getById(userId, itemId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void createItemTest() throws Exception {
        long userId = 1L;
        ItemDto requestDto = new ItemDto();
        requestDto.setName("test-item");
        requestDto.setDescription("test-item-desc");
        requestDto.setAvailable(true);
        requestDto.setOwnerId(1L);

        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.create(userId, requestDto)).thenReturn(expectedResponse);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateItemTest() throws Exception {
        long userId = 1L;
        Long itemId = 1L;
        UpdatedItemDto requestDto = new UpdatedItemDto();
        requestDto.setName("test-item");
        requestDto.setDescription("test-item-desc");
        requestDto.setAvailable(true);
        requestDto.setOwnerId(1L);

        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.update(itemId, userId, requestDto)).thenReturn(expectedResponse);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

}
