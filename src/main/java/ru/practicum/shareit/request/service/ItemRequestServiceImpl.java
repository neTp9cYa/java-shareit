package ru.practicum.shareit.request.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.pagination.FlexPageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestViewDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestViewDto create(final int userId, final ItemRequestCreateDto itemRequestCreateDto) {

        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        final ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestCreateDto, user);
        final ItemRequest storedItemRequest = itemRequestRepository.save(itemRequest);

        return itemRequestMapper.toItemRequestDetailViewDto(storedItemRequest, Collections.EMPTY_LIST);
    }

    @Override
    public List<ItemRequestViewDto> findOwn(final int userId) {

        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        final List<ItemRequest> itemRequests = itemRequestRepository.findByOwner_Id(userId);
        final List<Item> items = itemRepository.findByRequest_Owner_Id(userId);

        return itemRequestMapper.toItemRequestSummaryViewDtoList(
            itemRequests,
            toItemsByRequestItems(items));

    }

    @Override
    public List<ItemRequestViewDto> findSomeoneElses(final int userId, final Pageable pageable) {

        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        final List<ItemRequest> itemRequests = itemRequestRepository.findByOwner_IdNot(userId, pageable);
        final List<Item> items = itemRepository.findByRequest_Owner_IdNot(userId);

        return itemRequestMapper.toItemRequestSummaryViewDtoList(
            itemRequests,
            toItemsByRequestItems(items));
    }

    @Override
    public ItemRequestViewDto findById(int userId, int requestId) {

        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        final ItemRequest itemRequest = itemRequestRepository.findById(requestId)
            .orElseThrow(() -> new NotFoundException(String.format("Request with id %d not found", requestId)));

        final List<Item> items = itemRepository.findByRequest_Id(requestId);

        return itemRequestMapper.toItemRequestDetailViewDto(itemRequest, items);
    }

    private Map<Integer, List<Item>> toItemsByRequestItems(final List<Item> items) {
        final Map<Integer, List<Item>> itemsByRequestItems = new HashMap<>();
        for (final Item item : items) {
            List<Item> itemsByRequestItem = itemsByRequestItems.getOrDefault(item.getRequest().getId(), null);
            if (itemsByRequestItem == null) {
                itemsByRequestItem = new ArrayList<>();
                itemsByRequestItems.put(item.getRequest().getId(), itemsByRequestItem);
            }
            itemsByRequestItem.add(item);
        }
        return itemsByRequestItems;
    }
}
