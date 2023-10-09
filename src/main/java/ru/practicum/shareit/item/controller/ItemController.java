package ru.practicum.shareit.item.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapperImpl itemMapper;

    @GetMapping
    public List<ItemDto> findOwn(@RequestHeader("X-Sharer-User-Id") final Integer userId) {
        final List<Item> items = itemService.findByUserId(userId);
        return itemMapper.toItemDtoList(items);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable final Integer itemId) {
        final Item item = itemService.findById(itemId);
        return itemMapper.toItemDto(item);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam final String text) {
        final List<Item> items = itemService.search(text);
        return itemMapper.toItemDtoList(items);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") final Integer userId,
                          @RequestBody final ItemDto itemDto) {
        final Item item = itemMapper.toItem(itemDto);
        item.setOwnerId(userId);

        final Item createdItem = itemService.create(item);
        return itemMapper.toItemDto(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") final Integer userId,
                          @PathVariable final Integer itemId,
                          @RequestBody final ItemDto itemDto) {
        final Item item = itemMapper.toItem(itemDto);
        item.setOwnerId(userId);
        item.setId(itemId);

        final Item updatedItem = itemService.update(item);
        return itemMapper.toItemDto(updatedItem);
    }
}
