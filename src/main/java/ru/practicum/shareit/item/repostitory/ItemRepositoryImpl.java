package ru.practicum.shareit.item.repostitory;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Optional<Item> getById(long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Collection<Item> getAll(long userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwnerId() == userId)
                .toList();
    }

    @Override
    public Item create(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        if (item.getId() == null) {
            throw new NotFoundException("Пожалуйста укажите id вещи");
        }

        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void delete(long id) {
        if (!items.containsKey(id)) {
            throw new NotFoundException("При попытке удаления, вещь с id=" + id + " не была найдена");
        }

        items.remove(id);
    }

    @Override
    public Collection<Item> search(long id, String text) {
        return getAll(id)
                .stream()
                .filter(item -> item.getAvailable() && item.getName().toLowerCase().contains(text.toLowerCase()))
                .toList();
    }

    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
