package ru.practicum.shareit.user.service;

import java.util.List;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserViewDto;

public interface UserService {
    List<UserViewDto> findAll();

    UserViewDto findById(final int userId);

    UserViewDto create(final UserCreateDto userCreateDto);

    UserViewDto update(final int userId, final UserUpdateDto userUpdateDto);

    void delete(final int userId);
}
