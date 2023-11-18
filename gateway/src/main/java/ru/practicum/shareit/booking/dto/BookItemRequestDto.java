package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.validator.Interval;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Interval
public class BookItemRequestDto {
    private long itemId;
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
}
