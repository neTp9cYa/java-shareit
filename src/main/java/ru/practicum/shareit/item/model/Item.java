package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

@Getter
@Setter
@ToString
@Builder
public class Item {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
}
