package ru.practicum.shareit.booking.service;

import java.util.List;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingViewDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public interface BookingMapper {
    BookingViewDto toBookingViewDto(final Booking booking);

    List<BookingViewDto> toBookingViewDtoList(final List<Booking> bookings);

    Booking toBooking(final BookItemRequestDto bookItemRequestDto, final User user, final Item item);
}
