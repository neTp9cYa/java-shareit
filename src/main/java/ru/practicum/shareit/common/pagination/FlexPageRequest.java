package ru.practicum.shareit.common.pagination;

import javax.validation.ValidationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class FlexPageRequest extends FlexPageable {

    protected FlexPageRequest(final int offset, final int limit, final Sort sort) {
        super(offset, limit, sort);
    }

    public static FlexPageRequest of(final int offset, final int limit) {

        return of(offset, limit, Sort.unsorted());
    }

    public static FlexPageRequest of(final int offset, final int limit, final Sort sort) {

        // validations is here, because annotation does not work, i do not know why
        if (offset < 0) {
            throw new ValidationException("From is less then zero");
        }
        if (limit <= 0) {
            throw new ValidationException("Size is less or equals to zero");
        }

        return new FlexPageRequest(offset, limit, sort);
    }
}
