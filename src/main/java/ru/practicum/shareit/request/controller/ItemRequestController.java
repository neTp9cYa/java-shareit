package ru.practicum.shareit.request.controller;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    @LogInputOutputAnnotaion
    public ItemRequestViewDto create(@RequestHeader("X-Sharer-User-Id") @NotNull final Integer userId,
                                     @RequestBody @Validated ItemRequestCreateDto requestCreateDto) {
        return itemRequestService.create(userId, requestCreateDto);
    }

    @GetMapping
    @LogInputOutputAnnotaion
    public List<ItemRequestViewDto> findOwn(@RequestHeader("X-Sharer-User-Id") @NotNull final Integer userId) {
        return itemRequestService.findOwn(userId);
    }

    @GetMapping("all")
    //@LogInputOutputAnnotaion
    public List<ItemRequestViewDto> findSomeoneElses(
        @RequestHeader("X-Sharer-User-Id") @NotNull final Integer userId,
        @RequestParam(defaultValue = "0") @Min(0) final int from,
        @RequestParam(defaultValue = Integer.MAX_VALUE + "") @Min(1) final Integer size) {

        return itemRequestService.findSomeoneElses(userId, FlexPageRequest.of(from, size));
    }

    @GetMapping("{requestId}")
    @LogInputOutputAnnotaion
    public ItemRequestViewDto findById(@RequestHeader("X-Sharer-User-Id") @NotNull final Integer userId,
                                       @PathVariable final int requestId) {
        return itemRequestService.findById(userId, requestId);
    }
}
