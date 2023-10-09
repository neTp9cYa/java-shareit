package ru.practicum.shareit.user.validator;

import ru.practicum.shareit.user.model.User;

public interface UserValidator {
    void validateCreate(User user);

    void validateUpdate(User user);

    void validateDelete(Integer userId);
}
