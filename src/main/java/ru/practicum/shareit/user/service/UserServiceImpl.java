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
        final User user = userMapper.toUser(userDto);
        final User storedUser = userRepository.create(user);
        return userMapper.toUserDto(storedUser);
    }

    @Override
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

        userRepository.update(storedUser);
        return userMapper.toUserDto(storedUser);
    }

    @Override
    public void delete(final int userId) {
        userRepository.delete(userId);
    }
}
