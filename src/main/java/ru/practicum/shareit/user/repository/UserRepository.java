package ru.practicum.shareit.user.repository;

import java.util.List;
import java.util.Optional;
import ru.practicum.shareit.user.model.User;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(final int userId);

    Optional<User> findByEmail(final String email);

    User create(final User user);

    void update(final User user);

    void delete(final int userId);
}
