package ru.practicum.shareit.user.validator;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

@Component
public class UserValidatorImpl implements UserValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    @Override
    public void validateCreate(final User user) {
        if (user.getId() != null) {
            throw new ValidationException("User id must be null");
        }
        if (user.getEmail() == null) {
            throw new ValidationException("Email must be set");
        }
        validateEmail(user.getEmail());
    }

    @Override
    public void validateUpdate(final User user) {
        if (user.getId() == null) {
            throw new ValidationException("User id must be set");
        }
        if (user.getEmail() != null) {
            validateEmail(user.getEmail());
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
