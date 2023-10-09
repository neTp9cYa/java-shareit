package ru.practicum.shareit.item.service;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validator.ItemValidator;
import ru.practicum.shareit.user.service.UserService;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;
    private final UserService userService;

    @Override
    public List<Item> findByUserId(final Integer userId) {
        return itemRepository.findByUserId(userId);
    }

    @Override
    public Item findById(final Integer itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }

    @Override
    public List<Item> search(final String text) {
        if (text == null || text.isEmpty()) {
            return Collections.<Item>emptyList();
        }
        return itemRepository.search(text);
    }

    @Override
    public Item create(final Item item) {
        itemValidator.validateCreate(item);

        // check if user exists
        userService.findById(item.getOwnerId());

        return itemRepository.create(item);
    }

    @Override
    public Item update(final Item item) {
        itemValidator.validateUpdate(item);

        // check if user exists
        userService.findById(item.getOwnerId());

        final Item storedItem = itemRepository.findById(item.getId())
            .orElseThrow(() -> {
                throw new NotFoundException(
                    String.format("Item with id $d not found for user with id %d", item.getId(), item.getOwnerId()));
            });
        if (item.getOwnerId() != storedItem.getOwnerId()) {
            throw new NotFoundException(
                String.format("Item with id $d not found for user with id %d", item.getId(), item.getOwnerId()));
        }

        // set current value for absent props
        if (item.getName() == null) {
            item.setName(storedItem.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(storedItem.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(storedItem.getAvailable());
        }

        return itemRepository.update(item);
    }
}
