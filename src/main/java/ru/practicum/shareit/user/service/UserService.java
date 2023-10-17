package ru.practicum.shareit.user.service;

import java.util.List;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    List<UserDto> findAll();

    UserDto findById(final int userId);

    UserDto create(final UserDto user);

    UserDto update(final UserDto user);

    void delete(final int userId);
}
