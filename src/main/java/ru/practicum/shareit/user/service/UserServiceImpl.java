package ru.practicum.shareit.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAll() {
        final List<User> users = userRepository.findAll();
        return userMapper.toUserDtoList(users);
    }

    @Override
    public UserDto findById(final int userId) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto create(final UserDto userDto) {
        userValidator.validateCreate(userDto);
        final User user = userMapper.toUser(userDto);
        final User storedUser = userRepository.create(user);
        return userMapper.toUserDto(storedUser);
    }

    @Override
    public UserDto update(final UserDto userDto) {
        userValidator.validateUpdate(userDto);

        // validate if user with id exists
        final User storedUser = userRepository.findById(userDto.getId())
            .orElseThrow(() -> {
                throw new NotFoundException(String.format("User with id $d not found", userDto.getId()));
            });

        final User user = userMapper.toUser(userDto);

        // set current value for absent props
        if (user.getName() == null) {
            user.setName(storedUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(storedUser.getEmail());
        }

        userRepository.update(user);
        return userMapper.toUserDto(user);
    }

    @Override
    public void delete(final int userId) {
        userValidator.validateDelete(userId);
        userRepository.delete(userId);
    }
}
