package ru.practicum.shareit.item.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentViewDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentViewDto toCommentViewDto(final Comment comment) {
        return CommentViewDto.builder()
            .id(comment.getId())
            .text(comment.getText())
            .authorName(comment.getAuthor().getName())
            .created(comment.getCreated())
            .build();
    }

    @Override
    public List<CommentViewDto> toCommentViewDtoList(final List<Comment> comments) {
        if (comments == null) {
            return null;
        }
        return comments.stream().map(this::toCommentViewDto).collect(Collectors.toList());
    }

    @Override
    public Comment toComment(final CommentCreateDto commentCreateDto, final User user, final Item item) {
        return Comment.builder()
            .text(commentCreateDto.getText())
            .created(LocalDateTime.now())
            .author(user)
            .item(item)
            .build();
    }
}