package ru.practicum.shareit.item.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private Map<Integer, Item> itemsById = new HashMap<>();
    private Map<Integer, Map<Integer, Item>> itemsByUserId = new HashMap<>();
    private Integer nextId = 1;

    @Override
    public List<Item> findByUserId(final int userId) {
        final Map<Integer, Item> userItems = itemsByUserId.get(userId);
        return userItems != null
            ? new ArrayList<>(userItems.values())
            : Collections.<Item>emptyList();
    }

    @Override
    public Optional<Item> findById(final int itemId) {
        return Optional.ofNullable(itemsById.get(itemId));
    }

    @Override
    public List<Item> search(final String text) {
        final String lowerText = text.toLowerCase();
        return itemsById.values().stream()
            .filter(item -> item.getAvailable())
            .filter(item -> (item.getName() != null && item.getName().toLowerCase().contains(lowerText)) ||
                (item.getDescription() != null && item.getDescription().toLowerCase().contains(lowerText)))
            .collect(Collectors.toList());
    }

    @Override
    public Item create(final Item item) {
        setNextId(item);

        itemsById.put(item.getId(), item);

        Map<Integer, Item> userItems = itemsByUserId.get(item.getOwnerId());
        if (userItems == null) {
            userItems = new HashMap<>();
            itemsByUserId.put(item.getOwnerId(), userItems);
        }
        userItems.put(item.getId(), item);

        return item;
    }

    @Override
    public void update(final Item item) {
        itemsById.put(item.getId(), item);
        itemsByUserId.get(item.getOwnerId()).put(item.getId(), item);
    }

    private void setNextId(final Item item) {
        item.setId(nextId);
        nextId++;
    }
}
