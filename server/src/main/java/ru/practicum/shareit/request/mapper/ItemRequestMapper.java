package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(User requestor, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestor(requestor);
        return itemRequest;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setRequestor(itemRequest.getRequestor());
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }

    public static ItemRequestWithItemsDto mapToItemRequestWithItems(ItemRequest itemRequest,
                                                                    Collection<ItemForItemRequestDto> items) {
        ItemRequestWithItemsDto itemRequestWithItems = new ItemRequestWithItemsDto();
        itemRequestWithItems.setId(itemRequest.getId());
        itemRequestWithItems.setDescription(itemRequest.getDescription());
        itemRequestWithItems.setRequestorId(itemRequest.getRequestor().getId());
        itemRequestWithItems.setCreated(itemRequest.getCreated());
        itemRequestWithItems.setItems(items);
        return itemRequestWithItems;
    }
}
