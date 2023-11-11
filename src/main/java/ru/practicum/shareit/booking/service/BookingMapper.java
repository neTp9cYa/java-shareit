package ru.practicum.shareit.booking.service;

import java.util.List;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingViewDto;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingMapper {
    BookingViewDto toBookingViewDto(Booking booking);

    List<BookingViewDto> toBookingViewDtoList(List<Booking> bookings);

    Booking toBooking(BookingCreateDto bookingCreateDto);
}
