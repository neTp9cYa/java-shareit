package ru.practicum.shareit.booking.service;

import java.util.List;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingViewDto;

public interface BookingService {
    BookingViewDto create(final int userId, final BookingCreateDto bookingCreateDto);

    BookingViewDto approveOrReject(final int userId, final Integer bookingId, final Boolean approved);

    BookingViewDto findById(final int userId, final Integer bookingId);

    List<BookingViewDto> findOwn(final int userId, final BookingState bookingStatusDto);

    List<BookingViewDto> findByItemOwner(final int userId, final BookingState bookingStatusDto);
}
