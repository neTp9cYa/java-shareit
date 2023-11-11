package ru.practicum.shareit.item.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingViewDto;
import ru.practicum.shareit.item.model.Comment;

@Getter
@Setter
@ToString
@Builder
public class ItemViewDto {
    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private ItemViewBookingDto lastBooking;

    private ItemViewBookingDto nextBooking;

    private List<CommentViewDto> comments;

}
