package ru.practicum.shareit.dto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class ServerItemDtoTest {

    @Test
    void mapToItemRequestDtoTest() {
        Item item = new Item();
        item.setId(10009L);
        item.setName("test-item");
        item.setDescription("test-item-desc");
        item.setAvailable(true);
        item.setOwnerId(45001L);

        ItemForItemRequestDto itemForRequest = ItemMapper.mapToItemForRequestDto(item);
        itemForRequest.setRequestId(107L);

        assertEquals(107L, itemForRequest.getRequestId());
    }

    @Test
    void itemForItemRequestDtoTest() {
        ItemForItemRequestDto item = new ItemForItemRequestDto();
        item.setId(10809L);
        item.setName("test-item");
        item.setDescription("test-item-desc");
        item.setAvailable(true);
        item.setOwnerId(45081L);
        item.setRequestId(990099L);

        assertEquals(990099L, item.getRequestId());

    }
}
