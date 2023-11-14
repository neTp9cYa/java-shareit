package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public interface ItemMapper {
    ItemDto toItemDto(final Item item);

    ItemViewDto toItemViewDto(final Item item,
                              final Booking lastBookings,
                              final Booking nextBookings);

    ItemViewDto toItemViewDto(final Item item,
                              final Booking lastBookings,
                              final Booking nextBookings,
                              final List<Comment> comments);

    List<ItemDto> toItemDtoList(final List<Item> items);

    Item toItem(final ItemDto itemDto, final User owner);
}
