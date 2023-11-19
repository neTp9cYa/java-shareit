package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public interface ItemMapper {

    ItemViewDto toItemViewDto(final Item item,
                              final Booking lastBookings,
                              final Booking nextBookings);

    ItemViewDto toItemViewDto(final Item item,
                              final Booking lastBookings,
                              final Booking nextBookings,
                              final List<Comment> comments);

    List<ItemViewDto> toItemViewDtoList(final List<Item> items);

    Item toItem(final ItemUpdateDto itemUpdateDto, final User owner);

    Item toItem(final ItemCreateDto itemCreateDto, final User owner);
}
