package ru.practicum.shareit.item.validator;

import ru.practicum.shareit.item.model.Item;

public interface ItemValidator {
    void validateCreate(Item item);

    void validateUpdate(Item item);

    void validateDelete(Integer itemId);
}
