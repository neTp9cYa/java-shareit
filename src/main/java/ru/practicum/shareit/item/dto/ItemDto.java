package ru.practicum.shareit.item.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ItemDto {
    @Null(groups = ItemDtoCreate.class)
    private Integer id;

    @NotNull(groups = ItemDtoCreate.class)
    @NotBlank(groups = ItemDtoCreate.class)
    private String name;

    @NotNull(groups = ItemDtoCreate.class)
    @NotBlank(groups = ItemDtoCreate.class)
    private String description;

    @NotNull(groups = ItemDtoCreate.class)
    @AssertTrue(groups = ItemDtoCreate.class)
    private Boolean available;
}
