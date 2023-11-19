package ru.practicum.shareit.user.service;

import java.util.List;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserViewDto;
import ru.practicum.shareit.user.model.User;

public interface UserMapper {
    UserViewDto toUserViewDto(final User user);

    List<UserViewDto> toUserViewDtoList(final List<User> users);

    User toUser(final UserCreateDto userCreateDto);

    User toUser(final UserUpdateDto userUpdateDto);
}
