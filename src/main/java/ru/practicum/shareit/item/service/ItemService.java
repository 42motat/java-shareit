package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto getById(long id);

    Collection<ItemDto> getAll(long userId);

    ItemDto create(long userId, ItemDto itemDto);

    ItemDto update(long id, long userId, UpdatedItemDto updatedItemDto);

    void delete(long id);

    Collection<ItemDto> search(long id, String text);
}
