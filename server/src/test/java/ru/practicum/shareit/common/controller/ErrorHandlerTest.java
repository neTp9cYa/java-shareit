package ru.practicum.shareit.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.common.exception.ErrorDto;
import ru.practicum.shareit.common.exception.ExceptionHelper;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.service.UserService;

@WebMvcTest(controllers = {UserController.class, ExceptionHelper.class})
class ErrorHandlerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    void whenValidationExceptionThrownThenReturnErrorDto() throws Exception {
        when(userService.findAll())
            .thenThrow(new ValidationException("error"));

        final MvcResult result = mvc
            .perform(get("/users")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();

        final String response = result.getResponse().getContentAsString();
        final ErrorDto errorDto = mapper.readValue(response, ErrorDto.class);

        assertThat(errorDto, notNullValue());
        assertThat(errorDto.getError(), is("error"));
    }

    @Test
    void whenNotFoundExceptionThrownThenReturnErrorDto() throws Exception {
        when(userService.findAll())
            .thenThrow(new NotFoundException("error"));

        final MvcResult result = mvc
            .perform(get("/users")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();

        final String response = result.getResponse().getContentAsString();
        final ErrorDto errorDto = mapper.readValue(response, ErrorDto.class);

        assertThat(errorDto, notNullValue());
        assertThat(errorDto.getError(), is("error"));
    }

    @Test
    void whenConflictExceptionThrownThenReturnErrorDto() throws Exception {
        when(userService.findAll())
            .thenThrow(new ConflictException("error"));

        final MvcResult result = mvc
            .perform(get("/users")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andReturn();

        final String response = result.getResponse().getContentAsString();
        final ErrorDto errorDto = mapper.readValue(response, ErrorDto.class);

        assertThat(errorDto, notNullValue());
        assertThat(errorDto.getError(), is("error"));
    }

    @Test
    void whenDataIntegrityViolationExceptionThrownThenReturnErrorDto() throws Exception {
        when(userService.findAll())
            .thenThrow(new DataIntegrityViolationException("error"));

        final MvcResult result = mvc
            .perform(get("/users")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andReturn();

        final String response = result.getResponse().getContentAsString();
        final ErrorDto errorDto = mapper.readValue(response, ErrorDto.class);

        assertThat(errorDto, notNullValue());
        assertThat(errorDto.getError(), is("error"));
    }

    @Test
    void whenRuntimeExceptionThrownThenReturnErrorDto() throws Exception {
        when(userService.findAll())
            .thenThrow(new RuntimeException("error"));

        final MvcResult result = mvc
            .perform(get("/users")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andReturn();

        final String response = result.getResponse().getContentAsString();
        final ErrorDto errorDto = mapper.readValue(response, ErrorDto.class);

        assertThat(errorDto, notNullValue());
        assertThat(errorDto.getError(), is("error"));
    }
}