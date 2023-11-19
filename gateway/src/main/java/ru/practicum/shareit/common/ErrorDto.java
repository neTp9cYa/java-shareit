package ru.practicum.shareit.common;

import lombok.Getter;

@Getter
public class ErrorDto {
    private final String error;
    private final String stackTrace;

    public ErrorDto(final String error) {
        this(error, null);
    }

    public ErrorDto(final String error, final String stackTrace) {
        this.error = error;
        this.stackTrace = stackTrace;
    }
}
