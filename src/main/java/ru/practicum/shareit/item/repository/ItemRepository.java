package ru.practicum.shareit.item.repository;

import java.util.List;
import java.util.Optional;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository {
    List<Item> findByUserId(final int userId);

    Optional<Item> findById(final int itemId);

    List<Item> search(final String text);

    Item create(final Item item);

    void update(final Item item);
}
