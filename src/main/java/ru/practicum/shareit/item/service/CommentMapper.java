package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentViewDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public interface CommentMapper {
    CommentViewDto toCommentViewDto(final Comment comment);

    List<CommentViewDto> toCommentViewDtoList(final List<Comment> comments);

    Comment toComment(final CommentCreateDto commentCreateDto, final User user, final Item item);
}
