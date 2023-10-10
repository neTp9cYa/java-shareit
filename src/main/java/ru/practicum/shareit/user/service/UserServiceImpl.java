package ru.practicum.shareit.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validator.UserValidator;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserValidator userValidator;
    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(final int userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
    }

    @Override
    public User create(final User user) {
        userValidator.validateCreate(user);
        return userRepository.create(user);
    }

    @Override
    public User update(final User user) {
        userValidator.validateUpdate(user);

        // validate if user with id exists
        final User storedUser = userRepository.findById(user.getId())
            .orElseThrow(() -> {
                throw new NotFoundException(String.format("User with id $d not found", user.getId()));
            });

        // set current value for absent props
        if (user.getName() == null) {
            user.setName(storedUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(storedUser.getEmail());
        }

        userRepository.update(user);
        return user;
    }

    @Override
    public void delete(final int userId) {
        userValidator.validateDelete(userId);
        userRepository.delete(userId);
    }
}
