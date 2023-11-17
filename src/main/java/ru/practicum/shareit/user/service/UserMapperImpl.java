package ru.practicum.shareit.user.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserViewDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserViewDto toUserViewDto(final User user) {
        return UserViewDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .build();
    }

    @Override
    public List<UserViewDto> toUserViewDtoList(final List<User> users) {
        return users.stream().map(this::toUserViewDto).collect(Collectors.toList());
    }

    @Override
    public User toUser(final UserCreateDto userCreateDto) {
        return User.builder()
            .email(userCreateDto.getEmail())
            .name(userCreateDto.getName())
            .build();
    }

    @Override
    public User toUser(final UserUpdateDto userUpdateDto) {
        return User.builder()
            .email(userUpdateDto.getEmail())
            .name(userUpdateDto.getName())
            .build();
    }
}
