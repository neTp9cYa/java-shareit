package ru.practicum.shareit.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.common.exception.ErrorDto;
import ru.practicum.shareit.common.exception.ExceptionHelper;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ErrorHandler {

    private final ExceptionHelper exceptionHelper;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleValidationException(final ValidationException e) {
        log.warn("Validation Exception occured", e);
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleNotFoundException(final NotFoundException e) {
        log.warn("Not Found Exception occured", e);
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleNotFoundException(final ConflictException e) {
        log.warn("Conflict Exception occured", e);
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleThrowable(final Throwable e) {
        log.error("Unexpected error occured", e);
        return new ErrorDto(
            e.getMessage(),
            exceptionHelper.getStackTrace(e));
    }
}
