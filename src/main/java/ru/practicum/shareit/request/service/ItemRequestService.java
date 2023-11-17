package ru.practicum.shareit.request.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestViewDto;

public interface ItemRequestService {
    ItemRequestViewDto create(final int userId, final ItemRequestCreateDto itemRequestCreateDto);

    List<ItemRequestViewDto> findOwn(final int userId);

    List<ItemRequestViewDto> findSomeoneElses(final int userId, final Pageable pageable);

    ItemRequestViewDto findById(final int userId, final int requestId);
}
