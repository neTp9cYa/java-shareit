package ru.practicum.shareit.booking.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingViewDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.user.service.UserMapper;

@Component
@RequiredArgsConstructor
public class BookingMapperImpl implements BookingMapper {

    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @Override
    public BookingViewDto toBookingViewDto(final Booking booking) {
        return BookingViewDto.builder()
            .id(booking.getId())
            .item(itemMapper.toItemDto(booking.getItem()))
            .booker(userMapper.toUserDto(booking.getBooker()))
            .start(booking.getStart())
            .end(booking.getEnd())
            .status(booking.getStatus())
            .build();
    }

    @Override
    public List<BookingViewDto> toBookingViewDtoList(final List<Booking> bookings) {
        return bookings.stream().map(this::toBookingViewDto).collect(Collectors.toList());
    }

    @Override
    public Booking toBooking(final BookingCreateDto bookingCreateDto) {
        return Booking.builder()
            .start(bookingCreateDto.getStart())
            .end(bookingCreateDto.getEnd())
            .build();
    }
}
