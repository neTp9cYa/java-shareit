package ru.practicum.shareit.item.service;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;

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
        userService.findById(userId);

        final Item item = itemMapper.toItem(itemDto);
        item.setOwnerId(userId);

        final Item storedItem = itemRepository.create(item);
        return itemMapper.toItemDto(storedItem);
    }

    @Override
    public ItemDto update(final Integer userId, final ItemDto itemDto) {

        // check if user exists
        userService.findById(userId);

        final Item storedItem = itemRepository.findById(itemDto.getId())
            .orElseThrow(() -> {
                throw new NotFoundException(
                    String.format("Item with id %d not found for user with id %d", itemDto.getId(), userId));
            });
        if (!storedItem.getOwnerId().equals(userId)) {
            throw new NotFoundException(
                String.format("Item with id %d not found for user with id %d", itemDto.getId(), userId));
        }

        final Item item = itemMapper.toItem(itemDto);
        item.setOwnerId(userId);

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

        itemRepository.update(item);
        return itemMapper.toItemDto(item);
    }
}
