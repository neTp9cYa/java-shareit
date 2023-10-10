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
import ru.practicum.shareit.common.LogInputOutputAnnotaion;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapperImpl;
import ru.practicum.shareit.item.service.ItemService;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapperImpl itemMapper;

    @GetMapping
    @LogInputOutputAnnotaion
    public List<ItemDto> findOwn(@RequestHeader("X-Sharer-User-Id") final Integer userId) {
        return itemService.findByUserId(userId);
    }

    @GetMapping("/{itemId}")
    @LogInputOutputAnnotaion
    public ItemDto findById(@PathVariable final Integer itemId) {
        return itemService.findById(itemId);
    }

    @GetMapping("/search")
    @LogInputOutputAnnotaion
    public List<ItemDto> search(@RequestParam final String text) {
        return itemService.search(text);
    }

    @PostMapping
    @LogInputOutputAnnotaion
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") final Integer userId,
                          @RequestBody final ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @LogInputOutputAnnotaion
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") final Integer userId,
                          @PathVariable final Integer itemId,
                          @RequestBody final ItemDto itemDto) {
        itemDto.setId(itemId);
        return itemService.update(userId, itemDto);
    }
}
