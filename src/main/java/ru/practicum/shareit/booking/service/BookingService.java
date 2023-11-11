package ru.practicum.shareit.booking.service;

import java.util.List;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingStatusDto;
import ru.practicum.shareit.booking.dto.BookingViewDto;

public interface BookingService {
    BookingViewDto create(final Integer userId, final BookingCreateDto bookingCreateDto);

    BookingViewDto approveOrReject(final Integer userId, final Integer bookingId, final Boolean approved);

    BookingViewDto findById(final Integer userId, final Integer bookingId);

    List<BookingViewDto> findOwn(final Integer userId, final BookingStatusDto bookingStatusDto);

    List<BookingViewDto> findByItemOwner(final Integer userId, final BookingStatusDto bookingStatusDto);
}
