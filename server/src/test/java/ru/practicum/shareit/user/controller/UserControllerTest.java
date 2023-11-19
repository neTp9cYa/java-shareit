package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import ru.practicum.shareit.common.exception.ExceptionHelper;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserViewDto;
import ru.practicum.shareit.user.service.UserService;

@WebMvcTest(controllers = {UserController.class, ExceptionHelper.class})
class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    private UserCreateDto userCreateDto1 = UserCreateDto.builder()
        .name("name_1")
        .email("email_1@email.email")
        .build();

    private UserUpdateDto userUpdateDto1 = UserUpdateDto.builder()
        .name("name_1")
        .email("email_1@email.email")
        .build();

    private UserViewDto userViewDto1 = UserViewDto.builder()
        .id(1)
        .name("name_1")
        .email("email_1@email.email")
        .build();

    @Test
    void whenRequestCorrectThenfindAllReturnSucess() throws Exception {
        when(userService.findAll())
            .thenReturn(List.of(userViewDto1));

        mvc.perform(get("/users")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(List.of(userViewDto1))));
    }

    @Test
    void whenRequestCorrectThenFidByIdReturnSucess() throws Exception {
        when(userService.findById(userViewDto1.getId()))
            .thenReturn(userViewDto1);

        mvc.perform(get("/users/" + userViewDto1.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(userViewDto1)));
    }

    @Test
    void whenRequestCorrectThenCreateReturnSucess() throws Exception {
        when(userService.create(any()))
            .thenReturn(userViewDto1);

        mvc.perform(post("/users")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userCreateDto1)))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(userViewDto1)));
    }

    @Test
    void whenRequestCorrectThenUpdateReturnSucess() throws Exception {
        when(userService.update(anyInt(), any()))
            .thenReturn(userViewDto1);

        mvc.perform(patch("/users/1")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userUpdateDto1)))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(userViewDto1)));
    }

    @Test
    void whenRequestCorrectThenDeleteReturnSucess() throws Exception {
        mvc.perform(delete("/users/1")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}