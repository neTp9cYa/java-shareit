package ru.practicum.shareit.item.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentViewDto;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentViewDto toCommentViewDto(Comment comment) {
        return CommentViewDto.builder()
            .id(comment.getId())
            .text(comment.getText())
            .authorName(comment.getAuthor().getName())
            .created(comment.getCreated())
            .build();
    }

    @Override
    public List<CommentViewDto> toCommentViewDtoList(List<Comment> comments) {
        return comments.stream().map(this::toCommentViewDto).collect(Collectors.toList());
    }

    @Override
    public Comment toComment(CommentCreateDto commentCreateDto) {
        return Comment.builder()
            .text(commentCreateDto.getText())
            .build();
    }
}