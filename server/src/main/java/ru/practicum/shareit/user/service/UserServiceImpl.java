package ru.practicum.shareit.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserViewDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserViewDto> findAll() {
        final List<User> users = userRepository.findAll();
        return userMapper.toUserViewDtoList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public UserViewDto findById(final int userId) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        return userMapper.toUserViewDto(user);
    }

    @Override
    @Transactional
    public UserViewDto create(final UserCreateDto userCreateDto) {
        final User user = userMapper.toUser(userCreateDto);
        final User storedUser = userRepository.save(user);
        return userMapper.toUserViewDto(storedUser);
    }

    @Override
    @Transactional
    public UserViewDto update(final int userId, final UserUpdateDto userUpdateDto) {

        // validate if user exists
        final User storedUser = userRepository.findById(userId)
            .orElseThrow(() -> {
                throw new NotFoundException(String.format("User with id $d not found", userId));
            });

        final User user = userMapper.toUser(userUpdateDto);

        // update passed fields to new values
        if (user.getName() != null) {
            storedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            storedUser.setEmail(user.getEmail());
        }

        userRepository.save(storedUser);
        return userMapper.toUserViewDto(storedUser);
    }

    @Override
    @Transactional
    public void delete(final int userId) {
        userRepository.deleteById(userId);
    }
}
