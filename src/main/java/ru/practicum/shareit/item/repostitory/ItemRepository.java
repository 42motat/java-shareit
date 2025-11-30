package ru.practicum.shareit.item.repostitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(" select i from Item i " +
           " where i.available = true and " +
            " i.ownerId = ?1 and " +
           " upper(i.name) like upper(concat('%', ?2, '%')) " +
           " or upper(i.description) like upper(concat('%', ?2, '%')) ")
    Collection<Item> search(Long id, String text);

    Collection<Item> findByOwnerId(Long ownerId);
}
