package ru.practicum.shareit.booking.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingViewDto;

public interface BookingService {
    BookingViewDto create(final int userId, final BookItemRequestDto bookItemRequestDto);

    BookingViewDto approveOrReject(final int userId, final int bookingId, final Boolean approved);

    BookingViewDto findById(final int userId, final int bookingId);

    List<BookingViewDto> findOwn(final int userId, final BookingState bookingState, final Pageable pageable);

    List<BookingViewDto> findByItemOwner(final int userId, final BookingState bookingStatusDto,
                                         final Pageable pageable);
}
