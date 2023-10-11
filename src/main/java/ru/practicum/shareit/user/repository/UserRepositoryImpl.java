package ru.practicum.shareit.user.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.user.model.User;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private Map<Integer, User> usersById = new HashMap<>();
    private Map<String, User> usersByEmail = new HashMap<>();
    private Integer nextId = 1;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(usersById.values());
    }

    @Override
    public Optional<User> findById(final int userId) {
        final User user = usersById.get(userId);
        if (user == null) {
            return Optional.empty();
        }

        final User userCopy = user.toBuilder().build();
        return Optional.of(userCopy);
    }

    @Override
    public User create(final User user) {
        if (usersByEmail.containsKey(user.getEmail().toLowerCase())) {
            throw new ConflictException(String.format("User with email %s already exists", user.getEmail()));
        }

        setNextId(user);
        usersById.put(user.getId(), user);
        usersByEmail.put(user.getEmail().toLowerCase(), user);
        return user;
    }

    @Override
    public void update(final User user) {
        final User storedUser = usersById.get(user.getId());

        // validate if other user has not same email
        if (!user.getEmail().equalsIgnoreCase(storedUser.getEmail())) {
            if (usersByEmail.containsKey(user.getEmail().toLowerCase())) {
                throw new ConflictException(String.format("User with email %s already exists", user.getEmail()));
            }
        }

        if (!user.getEmail().equalsIgnoreCase(storedUser.getEmail())) {
            usersByEmail.remove(storedUser.getEmail().toLowerCase());
        }
        usersById.put(user.getId(), user);
        usersByEmail.put(user.getEmail().toLowerCase(), user);
    }

    @Override
    public void delete(final int userId) {
        final User storedUser = usersById.get(userId);
        if (storedUser == null) {
            return;
        }

        usersById.remove(storedUser.getId());
        usersByEmail.remove(storedUser.getEmail().toLowerCase());
    }

    private void setNextId(final User user) {
        user.setId(nextId);
        nextId++;
    }
}
