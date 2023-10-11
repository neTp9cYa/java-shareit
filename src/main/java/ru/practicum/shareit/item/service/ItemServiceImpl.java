package ru.practicum.shareit.item.service;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> findByUserId(final Integer userId) {
        final List<Item> items = itemRepository.findByUserId(userId);
        return itemMapper.toItemDtoList(items);
    }

    @Override
    public ItemDto findById(final Integer itemId) {
        final Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException(String.format("Item with id %d not found", itemId)));
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> search(final String text) {
        if (text == null || text.isEmpty()) {
            return Collections.<ItemDto>emptyList();
        }
        final List<Item> items = itemRepository.search(text);
        return itemMapper.toItemDtoList(items);
    }

    @Override
    public ItemDto create(final Integer userId, final ItemDto itemDto) {

        // check if user exists
        userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        final Item item = itemMapper.toItem(itemDto);
        item.setOwner(User.builder().id(userId).build());

        final Item storedItem = itemRepository.create(item);
        return itemMapper.toItemDto(storedItem);
    }

    @Override
    public ItemDto update(final Integer userId, final ItemDto itemDto) {

        // check if user exists
        userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        // check if item exists
        final Item storedItem = itemRepository.findById(itemDto.getId())
            .orElseThrow(() -> {
                throw new NotFoundException(
                    String.format("Item with id %d not found for user with id %d", itemDto.getId(), userId));
            });

        // check if user is owner of item
        if (!storedItem.getOwner().getId().equals(userId)) {
            throw new NotFoundException(
                String.format("Item with id %d not found for user with id %d", itemDto.getId(), userId));
        }

        final Item item = itemMapper.toItem(itemDto);
        item.setOwner(User.builder().id(userId).build());

        // update passed fields to new values
        if (item.getName() != null) {
            storedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            storedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            storedItem.setAvailable(item.getAvailable());
        }

        itemRepository.update(storedItem);
        return itemMapper.toItemDto(storedItem);
    }
}
