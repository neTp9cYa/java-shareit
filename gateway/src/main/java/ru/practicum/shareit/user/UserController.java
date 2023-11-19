package ru.practicum.shareit.user;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.common.LogInputOutputAnnotaion;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> addUser(
        @RequestBody @Valid final UserCreateDto userCreateDto) {

        return userClient.addUser(userCreateDto);
    }

    @GetMapping("/{userId}")
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> getUser(
        @PathVariable final long userId) {

        return userClient.getUser(userId);
    }

    @GetMapping
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> getUsers() {

        return userClient.getUsers();
    }

    @PatchMapping("/{userId}")
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> update(
        @PathVariable final long userId,
        @RequestBody @Valid final UserUpdateDto userUpdateDto) {

        return userClient.update(userId, userUpdateDto);
    }

    @DeleteMapping("/{userId}")
    @LogInputOutputAnnotaion
    public ResponseEntity<Object> delete(
        @PathVariable final int userId) {

        return userClient.delete(userId);
    }
}
