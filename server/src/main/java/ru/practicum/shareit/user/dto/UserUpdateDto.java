package ru.practicum.shareit.user.dto;

import javax.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class UserUpdateDto {

    private String name;

    @Email
    private String email;
}
