package ru.practicum.shareit.request.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestViewDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor
public class ItemRequestMapperImpl implements ItemRequestMapper {

    private final ItemMapper itemMapper;

    @Override
    public ItemRequest toItemRequest(final ItemRequestCreateDto itemRequestCreateDto, final User owner) {
        return ItemRequest.builder()
            .owner(owner)
            .description(itemRequestCreateDto.getDescription())
            .created(LocalDateTime.now())
            .build();
    }

    @Override
    public ItemRequestViewDto toItemRequestDetailViewDto(final ItemRequest itemRequest, final List<Item> items) {
        return ItemRequestViewDto.builder()
            .id(itemRequest.getId())
            .description(itemRequest.getDescription())
            .created(itemRequest.getCreated())
            .items(items != null ? itemMapper.toItemViewDtoList(items) : Collections.EMPTY_LIST)
            .build();
    }

    @Override
    public List<ItemRequestViewDto> toItemRequestSummaryViewDtoList(final List<ItemRequest> itemRequests,
                                                                    final Map<Integer, List<Item>> itemsByItemRequests) {
        return itemRequests.stream()
            .map(itemRequest -> toItemRequestDetailViewDto(
                itemRequest,
                itemsByItemRequests.getOrDefault(itemRequest.getId(), null)))
            .collect(Collectors.toList());
    }
}
