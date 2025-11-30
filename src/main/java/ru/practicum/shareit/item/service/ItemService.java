package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingAndCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;

import java.util.Collection;

public interface ItemService {
    ItemBookingAndCommentDto getById(long id, long userId);

    Collection<ItemDto> getAll(long userId);

    ItemDto create(long userId, ItemDto itemDto);

    ItemDto update(long id, long userId, UpdatedItemDto updatedItemDto);

    void delete(long id);

    Collection<ItemDto> search(long id, String text);

    CommentDto createComment(long id, long userId, String text);
}
