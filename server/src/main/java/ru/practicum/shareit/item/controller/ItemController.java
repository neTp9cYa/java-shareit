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
import ru.practicum.shareit.common.pagination.FlexPageRequest;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentViewDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.item.service.ItemService;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    @LogInputOutputAnnotaion
    public List<ItemViewDto> findOwn(@RequestHeader("X-Sharer-User-Id") final int userId, @RequestParam final int from,
                                     @RequestParam final int size) {
        return itemService.findByUserId(userId, FlexPageRequest.of(from, size));
    }

    @GetMapping("/{itemId}")
    @LogInputOutputAnnotaion
    public ItemViewDto findById(@RequestHeader("X-Sharer-User-Id") final int userId, @PathVariable final int itemId) {
        return itemService.findById(userId, itemId);
    }

    @GetMapping("/search")
    @LogInputOutputAnnotaion
    public List<ItemViewDto> search(@RequestParam final String text, @RequestParam final int from,
                                    @RequestParam final int size) {
        return itemService.search(text, FlexPageRequest.of(from, size));
    }

    @PostMapping
    @LogInputOutputAnnotaion
    public ItemViewDto create(@RequestHeader("X-Sharer-User-Id") final int userId,
                              @RequestBody final ItemCreateDto itemCreateDto) {
        return itemService.create(userId, itemCreateDto);
    }

    @PatchMapping("/{itemId}")
    @LogInputOutputAnnotaion
    public ItemViewDto update(@RequestHeader("X-Sharer-User-Id") final int userId, @PathVariable final int itemId,
                              @RequestBody final ItemUpdateDto itemUpdateDto) {
        return itemService.update(userId, itemId, itemUpdateDto);
    }

    @PostMapping("{itemId}/comment")
    @LogInputOutputAnnotaion
    public CommentViewDto addComment(@RequestHeader("X-Sharer-User-Id") final int userId,
                                     @PathVariable final int itemId,
                                     @RequestBody final CommentCreateDto commentCreateDto) {
        return itemService.addComment(userId, itemId, commentCreateDto);
    }
}
