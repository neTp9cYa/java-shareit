package ru.practicum.shareit.user.dto;

import javax.validation.constraints.Email;
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
public class UserDto {
    @Null(groups = {UserDtoCreate.class, UserDtoUpdate.class})
    private Integer id;


    private String name;

    @Email(groups = {UserDtoCreate.class, UserDtoUpdate.class})
    @NotNull(groups = UserDtoCreate.class)
    private String email;
}
