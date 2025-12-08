package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestWithItemsDto getById(Long userId, Long itemRequestId);

    Collection<ItemRequestWithItemsDto> getAllItemRequestsOfUser(Long userId);

    Collection<ItemRequestDto> getAllRequestsOfOtherUsers(Long userId);

    ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto);
}
