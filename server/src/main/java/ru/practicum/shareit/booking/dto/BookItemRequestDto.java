package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class BookItemRequestDto {
    private Integer itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
