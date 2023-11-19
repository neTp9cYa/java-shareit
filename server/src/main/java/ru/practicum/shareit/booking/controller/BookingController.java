package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
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
    public BookingViewDto bookItem(@RequestHeader("X-Sharer-User-Id") final int userId,
                                   @RequestBody final BookItemRequestDto bookItemRequestDto) {
        return bookingService.create(userId, bookItemRequestDto);
    }

    @GetMapping("{bookingId}")
    @LogInputOutputAnnotaion
    public BookingViewDto getBooking(@RequestHeader("X-Sharer-User-Id") final int userId,
                                     @PathVariable final int bookingId) {
        return bookingService.findById(userId, bookingId);
    }

    @GetMapping()
    @LogInputOutputAnnotaion
    public List<BookingViewDto> getOwnBookings(@RequestHeader("X-Sharer-User-Id") final int userId,
                                               @RequestParam final BookingState state, @RequestParam final int from,
                                               @RequestParam final int size) {
        return bookingService.findOwn(userId, state, FlexPageRequest.of(from, size));
    }

    @GetMapping("owner")
    @LogInputOutputAnnotaion
    public List<BookingViewDto> getOwnItemBookings(@RequestHeader("X-Sharer-User-Id") final int userId,
                                                   @RequestParam final BookingState state, @RequestParam final int from,
                                                   @RequestParam final int size) {
        return bookingService.findByItemOwner(userId, state, FlexPageRequest.of(from, size));
    }

    @PatchMapping("{bookingId}")
    @LogInputOutputAnnotaion
    public BookingViewDto approveOrReject(@RequestHeader("X-Sharer-User-Id") final int userId,
                                          @PathVariable final int bookingId,
                                          @RequestParam(name = "approved") final boolean approved) {
        return bookingService.approveOrReject(userId, bookingId, approved);
    }
}
