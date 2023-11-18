package ru.practicum.shareit.booking.dto.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

public class IntervalValidator implements ConstraintValidator<Interval, BookingCreateDto> {
    @Override
    public boolean isValid(BookingCreateDto bookingCreateDto, ConstraintValidatorContext constraintValidatorContext) {
        if (bookingCreateDto.getEnd() == null || bookingCreateDto.getStart() == null) {
            // not applicable
            return true;
        }
        return bookingCreateDto.getEnd().isAfter(bookingCreateDto.getStart());
    }
}
