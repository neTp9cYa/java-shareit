package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.user.dto.UserViewDto;

@Getter
@Setter
@ToString
@Builder
public class BookingViewDto {
    private Integer id;
    private ItemViewDto item;
    private UserViewDto booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
}
