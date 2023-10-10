package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemService {

    List<ItemDto> findByUserId(final Integer userId);

    ItemDto findById(final Integer itemId);

    List<ItemDto> search(final String text);

    ItemDto create(final Integer userId, final ItemDto itemDto);

    ItemDto update(final Integer userId, final ItemDto itemDto);
}
