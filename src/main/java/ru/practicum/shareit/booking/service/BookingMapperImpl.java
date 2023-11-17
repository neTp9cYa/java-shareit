package ru.practicum.shareit.booking.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingViewDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.user.model.User;
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
            .item(itemMapper.toItemViewDto(booking.getItem(), null, null))
            .booker(userMapper.toUserViewDto(booking.getBooker()))
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
    public Booking toBooking(final BookingCreateDto bookingCreateDto, final User user, final Item item) {
        return Booking.builder()
            .start(bookingCreateDto.getStart())
            .end(bookingCreateDto.getEnd())
            .status(BookingStatus.WAITING)
            .booker(user)
            .item(item)
            .build();
    }
}
