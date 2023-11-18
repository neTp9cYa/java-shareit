package ru.practicum.shareit.request;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.common.LogInputOutputAnnotaion;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> addItemRequest(
        @RequestHeader("X-Sharer-User-Id") final long userId,
        @RequestBody @Valid ItemRequestCreateDto itemRequestCreateDto) {

        return itemRequestClient.addItemRequest(userId, itemRequestCreateDto);
    }

    @GetMapping("{requestId}")
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> getItemRequest(
        @RequestHeader("X-Sharer-User-Id") final long userId,
        @PathVariable final long requestId) {

        return itemRequestClient.getItemRequest(userId, requestId);
    }

    @GetMapping
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> getOwnItemRequests(
        @RequestHeader("X-Sharer-User-Id") final long userId) {

        return itemRequestClient.getOwnItemRequests(userId);
    }

    @GetMapping("all")
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> getItemRequests(
        @RequestHeader("X-Sharer-User-Id") final long userId,
        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero final int from,
        @RequestParam(name = "size", defaultValue = "10") @Positive final int size) {

        return itemRequestClient.getItemRequests(userId, from, size);
    }
}
