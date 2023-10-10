package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;

@Component
public class ItemValidatorImpl implements ItemValidator {

    @Override
    public void validateCreate(final ItemDto itemDto) {
        if (itemDto.getId() != null) {
            throw new ValidationException("Item id must be null");
        }
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new ValidationException("Name must be set");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new ValidationException("Description must be set");
        }
        if (itemDto.getAvailable() == null || itemDto.getAvailable() == false) {
            throw new ValidationException("Item must be available");
        }
    }

    @Override
    public void validateUpdate(final ItemDto itemDto) {
        if (itemDto.getId() == null) {
            throw new ValidationException("Item id must be set");
        }
    }

    @Override
    public void validateDelete(final Integer itemId) {
        if (itemId == null) {
            throw new ValidationException("Item id must be set");
        }
    }
}



