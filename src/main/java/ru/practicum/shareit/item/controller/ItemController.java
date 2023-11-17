package ru.practicum.shareit.item.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
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
    public List<ItemViewDto> findOwn(@RequestHeader("X-Sharer-User-Id") final int userId,
                                     @RequestParam(defaultValue = "0") @Min(0) final int from,
                                     @RequestParam(defaultValue = Integer.MAX_VALUE + "") @Min(1) final Integer size) {
        return itemService.findByUserId(userId, FlexPageRequest.of(from, size));
    }

    @GetMapping("/{itemId}")
    @LogInputOutputAnnotaion
    public ItemViewDto findById(@RequestHeader("X-Sharer-User-Id") final int userId,
                                @PathVariable @NotNull final Integer itemId) {
        return itemService.findById(userId, itemId);
    }

    @GetMapping("/search")
    @LogInputOutputAnnotaion
    public List<ItemViewDto> search(@RequestParam final String text,
                                    @RequestParam(defaultValue = "0") @Min(0) final int from,
                                    @RequestParam(defaultValue = Integer.MAX_VALUE + "") @Min(1) final Integer size) {
        return itemService.search(text, FlexPageRequest.of(from, size));
    }

    @PostMapping
    @LogInputOutputAnnotaion
    public ItemViewDto create(@RequestHeader("X-Sharer-User-Id") final int userId,
                              @RequestBody @Validated final ItemCreateDto itemCreateDto) {
        return itemService.create(userId, itemCreateDto);
    }

    @PatchMapping("/{itemId}")
    @LogInputOutputAnnotaion
    public ItemViewDto update(@RequestHeader("X-Sharer-User-Id") final int userId,
                              @PathVariable @NotNull final Integer itemId,
                              @RequestBody @Validated final ItemUpdateDto itemUpdateDto) {
        return itemService.update(userId, itemId, itemUpdateDto);
    }

    @PostMapping("{itemId}/comment")
    @LogInputOutputAnnotaion
    public CommentViewDto addComment(@RequestHeader("X-Sharer-User-Id") final int userId,
                                     @PathVariable @NotNull final Integer itemId,
                                     @RequestBody @Valid final CommentCreateDto commentCreateDto) {
        return itemService.addComment(userId, itemId, commentCreateDto);
    }
}
