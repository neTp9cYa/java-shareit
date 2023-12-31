package ru.practicum.shareit.booking.dto.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = IntervalValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Interval {
    String message() default "End date is earlier than start date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
