package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repostitory.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto getById(long id) {
        Item item = itemRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id=" + id + " не найдена"));
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public Collection<ItemDto> getAll(long userId) {
        Collection<Item> items = itemRepository.getAll(userId);
        return items.stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не найден"));
        Item item = ItemMapper.mapToItem(user.getId(), itemDto);
        itemRepository.create(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto update(long id, long userId, UpdatedItemDto updatedItemDto) {
        userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не найден"));

        Item updatingItem = itemRepository.getById(id)
                .map(item -> ItemMapper.updateItemFields(item, updatedItemDto))
                .orElseThrow(() -> new NotFoundException("Такая вещь не найдена"));

        long itemOwnerId = updatingItem.getOwnerId();

        if (itemOwnerId != userId) {
            throw new BadRequest("Вы не можете обновить вещь, которая вам не принадлежит");
        }

        itemRepository.update(updatingItem);
        return ItemMapper.mapToItemDto(updatingItem);
    }

    @Override
    public void delete(long id) {
        itemRepository.delete(id);
    }

    @Override
    public Collection<ItemDto> search(long id, String text) {
        return itemRepository.search(id, text)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }
}
