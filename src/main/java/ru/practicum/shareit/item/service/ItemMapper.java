package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public interface ItemMapper {
    ItemDto toItemDto(final Item item);

    ItemViewDto toItemViewDto(final Item item);

    List<ItemDto> toItemDtoList(final List<Item> items);

    Item toItem(final ItemDto itemDto, final User owner);
}
