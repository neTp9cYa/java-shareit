package ru.practicum.shareit.request.service;

import java.util.List;
import java.util.Map;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestViewDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

public interface ItemRequestMapper {
    ItemRequest toItemRequest(final ItemRequestCreateDto itemRequestCreateDto, final User user);

    ItemRequestViewDto toItemRequestDetailViewDto(final ItemRequest itemRequest, final List<Item> items);

    List<ItemRequestViewDto> toItemRequestSummaryViewDtoList(final List<ItemRequest> itemRequests,
                                                             final Map<Integer, List<Item>> itemsByItemRequests);
}
