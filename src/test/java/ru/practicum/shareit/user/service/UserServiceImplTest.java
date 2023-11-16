package ru.practicum.shareit.user.service;

import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserViewDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAllReturnUsers() {
        final User user = getValidUser();

        Mockito
            .when(userRepository.findAll())
            .thenReturn(List.of(user));

        userService.findAll();

        Mockito.verify(userRepository, Mockito.times(1))
            .findAll();

        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void whenUserNotExistsThenFindByIdThrown() {
        final NotFoundException exception = Assertions.assertThrows(
            NotFoundException.class,
            () -> userService.findById(1));

        Assertions.assertEquals(
            String.format("User with id %d not found", 1),
            exception.getMessage());
    }

    @Test
    void whenUserExistsThenFindByIdReturnUser() {
        final User user = getValidUser();

        Mockito
            .when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        final UserViewDto userViewDto = userService.findById(user.getId());

        assertThat(userViewDto, notNullValue());
        assertThat(userViewDto.getId(), equalTo(user.getId()));
    }

    @Test
    void createSuccessfull() {
        final UserCreateDto userCreateDto = getValidUserCreateDto();
        final User user = getValidUser();

        Mockito
            .when(userRepository.save(any()))
            .thenReturn(user);

        final UserViewDto userViewDto = userService.create(userCreateDto);

        assertThat(userViewDto, notNullValue());
        assertThat(userViewDto.getId(), equalTo(user.getId()));
    }

    @Test
    void updateSuccessfull() {
        final UserUpdateDto userUpdateDto = getValidUserUpdateDto();
        final User user = getValidUser();

        Mockito
            .when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        Mockito
            .when(userRepository.save(any()))
            .thenReturn(user);

        final UserViewDto userViewDto = userService.update(user.getId(), userUpdateDto);

        assertThat(userViewDto, notNullValue());
        assertThat(userViewDto.getId(), equalTo(user.getId()));
    }

    @Test
    void deleteSuccessfull() {
        userService.delete(1);

        Mockito.verify(userRepository, Mockito.times(1))
            .deleteById(1);
    }

    private User getValidUser() {
        return User.builder()
            .id(1)
            .name("name")
            .email("email@email.email")
            .build();
    }

    private UserCreateDto getValidUserCreateDto() {
        return UserCreateDto.builder()
            .name("name_1")
            .email("email_1@email.email")
            .build();
    }

    private UserUpdateDto getValidUserUpdateDto() {
        return UserUpdateDto.builder()
            .name("name_1")
            .email("email_1@email.email")
            .build();
    }
}