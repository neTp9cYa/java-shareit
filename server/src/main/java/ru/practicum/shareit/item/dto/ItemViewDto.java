package ru.practicum.shareit.item.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ItemViewDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
    private ItemViewBookingDto lastBooking;
    private ItemViewBookingDto nextBooking;
    private List<CommentViewDto> comments;
}
