package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemMapperImpl {
    ItemDto toItemDto(Item item);

    List<ItemDto> toItemDtoList(List<Item> items);

    Item toItem(ItemDto itemDto);
}
