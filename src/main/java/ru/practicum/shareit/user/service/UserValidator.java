package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserValidator {
    void validateCreate(UserDto userDto);

    void validateUpdate(UserDto userDto);

    void validateDelete(Integer userId);
}
