package ru.practicum.shareit.user.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.common.LogInputOutputAnnotaion;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    @LogInputOutputAnnotaion
    public List<UserDto> findAll() {
        log.info("Start to process findAll action from UserController with no parameters");
        final List<User> users = userService.findAll();
        final List<UserDto> usersDto = userMapper.toUserDtoList(users);
        log.info("End to process findAll action from UserController with result: {}", usersDto);
        return usersDto;
    }

    @GetMapping("/{userId}")
    @LogInputOutputAnnotaion
    public UserDto findById(@PathVariable final Integer userId) {
        final User user = userService.findById(userId);
        return userMapper.toUserDto(user);
    }

    @PostMapping
    @LogInputOutputAnnotaion
    public UserDto create(@RequestBody final UserDto userDto) {
        final User user = userMapper.toUser(userDto);
        final User createdUser = userService.create(user);
        return userMapper.toUserDto(createdUser);
    }

    @PatchMapping("/{userId}")
    @LogInputOutputAnnotaion
    public UserDto update(@PathVariable final Integer userId,
                          @RequestBody final UserDto userDto) {
        final User user = userMapper.toUser(userDto);
        user.setId(userId);

        final User updatedUser = userService.update(user);
        return userMapper.toUserDto(updatedUser);
    }

    @DeleteMapping("/{userId}")
    @LogInputOutputAnnotaion
    public void delete(@PathVariable final Integer userId) {
        userService.delete(userId);
    }
}
