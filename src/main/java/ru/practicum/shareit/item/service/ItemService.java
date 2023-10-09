package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.item.model.Item;

public interface ItemService {

    List<Item> findByUserId(final Integer userId);

    Item findById(final Integer itemId);

    List<Item> search(final String text);

    Item create(final Item user);

    Item update(final Item user);
}
