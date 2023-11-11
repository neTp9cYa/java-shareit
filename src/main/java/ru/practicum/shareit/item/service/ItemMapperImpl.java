package ru.practicum.shareit.item.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapperImpl implements ItemMapper {
    @Override
    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .available(item.getAvailable())
            .build();
    }

    @Override
    public ItemViewDto toItemViewDto(Item item) {
        return ItemViewDto.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .available(item.getAvailable())
            .build();
    }

    @Override
    public List<ItemDto> toItemDtoList(final List<Item> items) {
        return items.stream().map(this::toItemDto).collect(Collectors.toList());
    }

    @Override
    public Item toItem(final ItemDto itemDto) {
        return Item.builder()
            .id(itemDto.getId())
            .name(itemDto.getName())
            .description(itemDto.getDescription())
            .available(itemDto.getAvailable())
            .build();
    }
}
