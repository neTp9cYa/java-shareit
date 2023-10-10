package ru.practicum.shareit.user.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDto toUserDto(User user) {
        return UserDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .build();
    }

    @Override
    public List<UserDto> toUserDtoList(final List<User> users) {
        return users.stream().map(this::toUserDto).collect(Collectors.toList());
    }

    @Override
    public User toUser(UserDto userDto) {
        return User.builder()
            .id(userDto.getId())
            .email(userDto.getEmail())
            .name(userDto.getName())
            .build();
    }
}
