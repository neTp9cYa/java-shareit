package ru.practicum.shareit.booking.controller;

import java.util.List;
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
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingViewDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.LogInputOutputAnnotaion;
import ru.practicum.shareit.common.pagination.FlexPageRequest;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @LogInputOutputAnnotaion
    public BookingViewDto create(@RequestHeader("X-Sharer-User-Id") final int userId,
                                 @RequestBody @Validated final BookingCreateDto bookingCreateDto) {
        return bookingService.create(userId, bookingCreateDto);
    }

    @PatchMapping("{bookingId}")
    @LogInputOutputAnnotaion
    public BookingViewDto approveOrReject(@RequestHeader("X-Sharer-User-Id") final int userId,
                                          @PathVariable @NotNull final Integer bookingId,
                                          @RequestParam @NotNull final Boolean approved) {
        return bookingService.approveOrReject(userId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    @LogInputOutputAnnotaion
    public BookingViewDto findById(@RequestHeader("X-Sharer-User-Id") final int userId,
                                   @PathVariable @NotNull final Integer bookingId) {
        return bookingService.findById(userId, bookingId);
    }

    @GetMapping()
    @LogInputOutputAnnotaion
    public List<BookingViewDto> findOwn(
        @RequestHeader("X-Sharer-User-Id") final int userId,
        @RequestParam(defaultValue = "ALL") final BookingState state,
        @RequestParam(defaultValue = "0") @Min(0) final int from,
        @RequestParam(defaultValue = Integer.MAX_VALUE + "") @Min(1) final Integer size) {

        return bookingService.findOwn(userId, state, FlexPageRequest.of(from, size));
    }

    @GetMapping("owner")
    @LogInputOutputAnnotaion
    public List<BookingViewDto> findByItemOwner(
        @RequestHeader("X-Sharer-User-Id") final int userId,
        @RequestParam(defaultValue = "ALL") final BookingState state,
        @RequestParam(defaultValue = "0") @Min(0) final int from,
        @RequestParam(defaultValue = Integer.MAX_VALUE + "") @Min(1) final Integer size) {

        return bookingService.findByItemOwner(userId, state, FlexPageRequest.of(from, size));
    }
}
