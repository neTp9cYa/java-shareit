package ru.practicum.shareit.user.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.common.LogInputOutputAnnotaion;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserViewDto;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    @LogInputOutputAnnotaion
    public List<UserViewDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    @LogInputOutputAnnotaion
    public UserViewDto findById(@PathVariable final int userId) {
        return userService.findById(userId);
    }

    @PostMapping
    @LogInputOutputAnnotaion
    public UserViewDto create(@RequestBody final UserCreateDto userCreateDto) {
        return userService.create(userCreateDto);
    }

    @PatchMapping("/{userId}")
    @LogInputOutputAnnotaion
    public UserViewDto update(@PathVariable final int userId,
                              @RequestBody final UserUpdateDto userUpdateDto) {
        return userService.update(userId, userUpdateDto);
    }

    @DeleteMapping("/{userId}")
    @LogInputOutputAnnotaion
    public void delete(@PathVariable final int userId) {
        userService.delete(userId);
    }
}
