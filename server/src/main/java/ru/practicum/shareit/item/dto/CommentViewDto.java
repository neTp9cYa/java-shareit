package ru.practicum.shareit.item.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class CommentViewDto {
    private Integer id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
