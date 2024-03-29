package ru.practicum.shareit.request.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.common.LogInputOutputAnnotaion;
import ru.practicum.shareit.common.pagination.FlexPageRequest;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestViewDto;
import ru.practicum.shareit.request.service.ItemRequestService;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    @LogInputOutputAnnotaion
    public ItemRequestViewDto create(@RequestHeader("X-Sharer-User-Id") final int userId,
                                     @RequestBody ItemRequestCreateDto requestCreateDto) {
        return itemRequestService.create(userId, requestCreateDto);
    }

    @GetMapping
    @LogInputOutputAnnotaion
    public List<ItemRequestViewDto> findOwn(@RequestHeader("X-Sharer-User-Id") final int userId) {
        return itemRequestService.findOwn(userId);
    }

    @GetMapping("all")
    @LogInputOutputAnnotaion
    public List<ItemRequestViewDto> findAll(@RequestHeader("X-Sharer-User-Id") final int userId,
                                            @RequestParam final int from, @RequestParam final int size) {
        return itemRequestService.findSomeoneElses(userId, FlexPageRequest.of(from, size));
    }

    @GetMapping("{requestId}")
    @LogInputOutputAnnotaion
    public ItemRequestViewDto findById(@RequestHeader("X-Sharer-User-Id") final int userId,
                                       @PathVariable final int requestId) {
        return itemRequestService.findById(userId, requestId);
    }
}
