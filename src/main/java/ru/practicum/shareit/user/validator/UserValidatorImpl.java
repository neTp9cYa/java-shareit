package ru.practicum.shareit.user.validator;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserValidatorImpl implements UserValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    @Override
    public void validateCreate(final UserDto userDto) {
        if (userDto.getId() != null) {
            throw new ValidationException("User id must be null");
        }
        if (userDto.getEmail() == null) {
            throw new ValidationException("Email must be set");
        }
        validateEmail(userDto.getEmail());
    }

    @Override
    public void validateUpdate(final UserDto userDto) {
        if (userDto.getId() == null) {
            throw new ValidationException("User id must be set");
        }
        if (userDto.getEmail() != null) {
            validateEmail(userDto.getEmail());
        }
    }

    @Override
    public void validateDelete(final Integer userId) {
        if (userId == null) {
            throw new ValidationException("User id must be set");
        }
    }

    private void validateEmail(final String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Email must be valid");
        }
    }
}
