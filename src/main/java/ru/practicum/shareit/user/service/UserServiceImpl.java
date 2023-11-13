package ru.practicum.shareit.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        final List<User> users = userRepository.findAll();
        return userMapper.toUserDtoList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(final int userId) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto create(final UserDto userDto) {
        final User user = userMapper.toUser(userDto);

        User storedUser;
        try {
            storedUser = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(String.format("User with email %s already exists", user.getEmail()));
        }

        return userMapper.toUserDto(storedUser);
    }

    @Override
    @Transactional
    public UserDto update(final UserDto userDto) {

        // validate if user exists
        final User storedUser = userRepository.findById(userDto.getId())
            .orElseThrow(() -> {
                throw new NotFoundException(String.format("User with id $d not found", userDto.getId()));
            });

        final User user = userMapper.toUser(userDto);

        // update passed fields to new values
        if (user.getName() != null) {
            storedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            storedUser.setEmail(user.getEmail());
        }

        try {
            userRepository.save(storedUser);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(String.format("User with email %s already exists", user.getEmail()));
        }

        return userMapper.toUserDto(storedUser);
    }

    @Override
    @Transactional
    public void delete(final int userId) {
        userRepository.deleteById(userId);
    }
}
