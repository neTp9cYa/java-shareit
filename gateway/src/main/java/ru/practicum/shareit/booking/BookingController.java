package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.common.LogInputOutputAnnotaion;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> bookItem(
        @RequestHeader("X-Sharer-User-Id") final long userId,
        @RequestBody @Valid final BookItemRequestDto requestDto) {

        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> getBooking(
        @RequestHeader("X-Sharer-User-Id") final long userId,
        @PathVariable final long bookingId) {

        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> getOwnBookings(
        @RequestHeader("X-Sharer-User-Id") final long userId,
        @RequestParam(name = "state", defaultValue = "all") final String stateParam,
        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero final int from,
        @RequestParam(name = "size", defaultValue = "10") @Positive final int size) {

        BookingState state = BookingState.from(stateParam)
            .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getOwnBookings(userId, state, from, size);
    }

    @GetMapping("owner")
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> getOwnItemsBookings(
        @RequestHeader("X-Sharer-User-Id") final long userId,
        @RequestParam(name = "state", defaultValue = "all") final String stateParam,
        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero final int from,
        @RequestParam(name = "size", defaultValue = "10") @Positive final int size) {

        BookingState state = BookingState.from(stateParam)
            .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getOwnItemsBookings(userId, state, from, size);
    }

    @PatchMapping("{bookingId}")
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> approveOrRejectBooking(
        @RequestHeader("X-Sharer-User-Id") final long userId,
        @PathVariable final long bookingId,
        @RequestParam(name = "approved") final boolean approved) {

        return bookingClient.approveOrRejectBooking(userId, bookingId, approved);
    }
}
