package ru.practicum.shareit.item.repostitory;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> getById(long id);

    Collection<Item> getAll(long userId);

    Item create(Item item);

    Item update(Item item);

    void delete(long id);

    Collection<Item> search(long id, String text);
}
