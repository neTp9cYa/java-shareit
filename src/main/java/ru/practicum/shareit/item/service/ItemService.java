package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentViewDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemViewDto;

public interface ItemService {

    List<ItemViewDto> findByUserId(final Integer userId);

    ItemViewDto findById(final Integer userId, final Integer itemId);

    List<ItemDto> search(final String text);

    ItemDto create(final Integer userId, final ItemDto itemDto);

    ItemDto update(final Integer userId, final ItemDto itemDto);

    CommentViewDto addComment(final Integer userId, final Integer itemId, final CommentCreateDto commentCreateDto);
}
