package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemValidator {
    void validateCreate(ItemDto itemDto);

    void validateUpdate(ItemDto itemDto);

    void validateDelete(Integer itemId);
}
