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
import ru.practicum.shareit.user.service.UserMapper;
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
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    @LogInputOutputAnnotaion
    public UserDto findById(@PathVariable final Integer userId) {
        return userService.findById(userId);
    }

    @PostMapping
    @LogInputOutputAnnotaion
    public UserDto create(@RequestBody final UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    @LogInputOutputAnnotaion
    public UserDto update(@PathVariable final Integer userId,
                          @RequestBody final UserDto userDto) {
        userDto.setId(userId);
        return userService.update(userDto);
    }

    @DeleteMapping("/{userId}")
    @LogInputOutputAnnotaion
    public void delete(@PathVariable final Integer userId) {
        userService.delete(userId);
    }
}
