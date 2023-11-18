package ru.practicum.shareit.booking.dto.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

public class IntervalValidator implements ConstraintValidator<Interval, BookItemRequestDto> {
    @Override
    public boolean isValid(BookItemRequestDto bookItemRequestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (bookItemRequestDto.getEnd() == null || bookItemRequestDto.getStart() == null) {
            // not applicable
            return true;
        }
        return bookItemRequestDto.getEnd().isAfter(bookItemRequestDto.getStart());
    }
}
