package ru.practicum.shareit.item.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private Map<Integer, Item> items = new HashMap<>();
    private Integer nextId = 1;

    @Override
    public List<Item> findByUserId(final int userId) {
        return items.values().stream()
            .filter(item -> item.getOwnerId() == userId)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> findById(final int itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> search(final String text) {
        final String lowerText = text.toLowerCase();
        return items.values().stream()
            .filter(item -> item.getAvailable())
            .filter(item -> (item.getName() != null && item.getName().toLowerCase().contains(lowerText)) ||
                (item.getDescription() != null && item.getDescription().toLowerCase().contains(lowerText)))
            .collect(Collectors.toList());
    }

    @Override
    public Item create(final Item item) {
        setNextId(item);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(final Item item) {
        items.put(item.getId(), item);
        return item;
    }

    private void setNextId(final Item item) {
        item.setId(nextId);
        nextId++;
    }
}
