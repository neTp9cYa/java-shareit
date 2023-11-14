package ru.practicum.shareit.item.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentViewDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping
    @LogInputOutputAnnotaion
    public List<ItemViewDto> findOwn(@RequestHeader("X-Sharer-User-Id") @NotNull final Integer userId) {
        return itemService.findByUserId(userId);
    }

    @GetMapping("/{itemId}")
    @LogInputOutputAnnotaion
    public ItemViewDto findById(@RequestHeader("X-Sharer-User-Id") @NotNull final Integer userId,
                                @PathVariable @NotNull final Integer itemId) {
        return itemService.findById(userId, itemId);
    }

    @GetMapping("/search")
    @LogInputOutputAnnotaion
    public List<ItemDto> search(@RequestParam final String text) {
        return itemService.search(text);
    }

    @PostMapping
    @LogInputOutputAnnotaion
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") @NotNull final Integer userId,
                          @RequestBody @Validated(ItemDtoCreate.class) final ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @LogInputOutputAnnotaion
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") @NotNull final Integer userId,
                          @PathVariable @NotNull final Integer itemId,
                          @RequestBody @Validated(ItemDtoUpdate.class) final ItemDto itemDto) {
        itemDto.setId(itemId);
        return itemService.update(userId, itemDto);
    }

    @PostMapping("{itemId}/comment")
    @LogInputOutputAnnotaion
    public CommentViewDto addComment(@RequestHeader("X-Sharer-User-Id") @NotNull final Integer userId,
                                     @PathVariable @NotNull final Integer itemId,
                                     @RequestBody @Valid final CommentCreateDto commentCreateDto) {
        return itemService.addComment(userId, itemId, commentCreateDto);
    }
}
