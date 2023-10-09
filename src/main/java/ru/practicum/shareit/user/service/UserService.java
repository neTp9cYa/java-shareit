package ru.practicum.shareit.user.service;

import java.util.List;
import ru.practicum.shareit.user.model.User;

public interface UserService {
    List<User> findAll();

    User findById(final int userId);

    User create(final User user);

    User update(final User user);

    void delete(final int userId);
}
