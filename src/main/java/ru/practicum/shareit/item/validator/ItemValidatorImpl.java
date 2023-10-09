package ru.practicum.shareit.item.validator;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemValidatorImpl implements ItemValidator {

    @Override
    public void validateCreate(final Item item) {
        if (item.getId() != null) {
            throw new ValidationException("Item id must be null");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Name must be set");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Description must be set");
        }
        if (item.getAvailable() == null || item.getAvailable() == false) {
            throw new ValidationException("Item must be available");
        }
    }

    @Override
    public void validateUpdate(final Item item) {
        if (item.getId() == null) {
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



