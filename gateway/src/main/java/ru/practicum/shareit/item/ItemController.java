package ru.practicum.shareit.item;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.common.LogInputOutputAnnotaion;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> addItem(
        @RequestHeader("X-Sharer-User-Id") final int userId,
        @RequestBody @Valid final ItemCreateDto itemCreateDto) {

        return itemClient.addItem(userId, itemCreateDto);
    }

    @GetMapping("/{itemId}")
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> getItem(
        @RequestHeader("X-Sharer-User-Id") final int userId,
        @PathVariable final int itemId) {

        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> getOwnItems(
        @RequestHeader("X-Sharer-User-Id") final int userId,
        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero final int from,
        @RequestParam(name = "size", defaultValue = "10") @Positive final int size) {

        return itemClient.getOwnItems(userId, from, size);
    }

    @GetMapping("/search")
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> search(
        @RequestHeader("X-Sharer-User-Id") final int userId,
        @RequestParam(name = "text") final String text,
        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero final int from,
        @RequestParam(name = "size", defaultValue = "10") @Positive final int size) {

        return itemClient.search(userId, text, from, size);
    }

    @PatchMapping("/{itemId}")
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> update(
        @RequestHeader("X-Sharer-User-Id") final int userId,
        @PathVariable final long itemId,
        @RequestBody @Valid final ItemUpdateDto itemUpdateDto) {

        return itemClient.update(userId, itemId, itemUpdateDto);
    }

    @PostMapping("{itemId}/comment")
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> addComment(
        @RequestHeader("X-Sharer-User-Id") final int userId,
        @PathVariable final int itemId,
        @RequestBody @Valid final CommentCreateDto commentCreateDto) {

        return itemClient.addComment(userId, itemId, commentCreateDto);
    }
}
