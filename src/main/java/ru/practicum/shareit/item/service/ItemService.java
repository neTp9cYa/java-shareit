package ru.practicum.shareit.item.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentViewDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemViewDto;

public interface ItemService {

    List<ItemViewDto> findByUserId(final Integer userId, final Pageable pageable);

    ItemViewDto findById(final Integer userId, final Integer itemId);

    List<ItemViewDto> search(final String text, final Pageable pageable);

    ItemViewDto create(final Integer userId, final ItemCreateDto itemCreateDto);

    ItemViewDto update(final Integer userId, final int itemId, final ItemUpdateDto itemUpdateDto);

    CommentViewDto addComment(final Integer userId, final Integer itemId, final CommentCreateDto commentCreateDto);
}
