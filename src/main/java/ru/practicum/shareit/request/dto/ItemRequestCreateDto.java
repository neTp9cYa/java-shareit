package ru.practicum.shareit.request.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequestCreateDto {
    @NotBlank
    private String description;
}
