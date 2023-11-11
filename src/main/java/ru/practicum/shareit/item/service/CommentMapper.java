package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentViewDto;
import ru.practicum.shareit.item.model.Comment;

public interface CommentMapper {
    CommentViewDto toCommentViewDto(Comment comment);

    List<CommentViewDto> toCommentViewDtoList(List<Comment> comments);

    Comment toComment(CommentCreateDto commentCreateDto);
}
