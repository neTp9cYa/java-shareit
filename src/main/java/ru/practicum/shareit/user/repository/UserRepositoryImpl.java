package ru.practicum.shareit.user.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;
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
        return Optional.ofNullable(usersById.get(userId));
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return Optional.ofNullable(usersByEmail.get(email.toLowerCase()));
    }

    @Override
    public User create(final User user) {
        setNextId(user);
        usersById.put(user.getId(), user);
        usersByEmail.put(user.getEmail().toLowerCase(), user);
        return user;
    }

    @Override
    public User update(final User user) {
        final User storedUser = usersById.get(user.getId());
        if (!user.getEmail().equalsIgnoreCase(storedUser.getEmail())) {
            usersByEmail.remove(storedUser.getEmail().toLowerCase());
        }
        usersById.put(user.getId(), user);
        usersByEmail.put(user.getEmail().toLowerCase(), user);
        return user;
    }

    @Override
    public void delete(final int userId) {
        final User storedUser = usersById.get(userId);
        usersById.remove(storedUser.getId());
        usersByEmail.remove(storedUser.getEmail().toLowerCase());
    }

    private void setNextId(final User user) {
        user.setId(nextId);
        nextId++;
    }
}
