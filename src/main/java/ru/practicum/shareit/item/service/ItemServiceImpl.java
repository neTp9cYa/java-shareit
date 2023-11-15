package ru.practicum.shareit.item.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentViewDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ItemViewDto> findByUserId(final Integer userId, final Pageable pageable) {
        final List<Item> items = itemRepository.findByOwner_Id(userId);

        final List<Booking> bookings =
            bookingRepository.findByItem_Owner_IdAndStatus(userId, BookingStatus.APPROVED, pageable);
        final Pair<Map<Integer, Booking>, Map<Integer, Booking>> nearestBookings = getNearestBookings(bookings);
        final Map<Integer, Booking> lastBookings = nearestBookings.getFirst();
        final Map<Integer, Booking> nextBookings = nearestBookings.getSecond();

        final List<ItemViewDto> itemViewDtos = items.stream()
            .map(item -> itemMapper.toItemViewDto(
                item,
                lastBookings.getOrDefault(item.getId(), null),
                nextBookings.getOrDefault(item.getId(), null)))
            .collect(Collectors.toList());

        return itemViewDtos;
    }

    private Pair<Map<Integer, Booking>, Map<Integer, Booking>> getNearestBookings(final List<Booking> bookings) {
        final Map<Integer, Booking> lastBookings = new HashMap<>();
        final Map<Integer, Booking> nextBookings = new HashMap<>();

        final LocalDateTime now = LocalDateTime.now();
        for (final Booking booking : bookings) {
            if (booking.getStart().isAfter(now)) {
                final Booking currentNextBooking = nextBookings.getOrDefault(booking.getItem().getId(), null);
                if (currentNextBooking == null) {
                    nextBookings.put(booking.getItem().getId(), booking);
                } else if (booking.getStart().isBefore(currentNextBooking.getStart())) {
                    nextBookings.put(booking.getItem().getId(), booking);
                }
            } else {
                final Booking currentLastBooking = lastBookings.getOrDefault(booking.getItem().getId(), null);
                if (currentLastBooking == null) {
                    lastBookings.put(booking.getItem().getId(), booking);
                } else if (booking.getStart().isAfter(currentLastBooking.getStart())) {
                    lastBookings.put(booking.getItem().getId(), booking);
                }
            }
        }

        return Pair.of(lastBookings, nextBookings);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemViewDto findById(final Integer userId, final Integer itemId) {
        final Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException(String.format("Item with id %d not found", itemId)));

        final List<Booking> bookings = bookingRepository.findByItem_Owner_IdAndStatus(
            userId,
            BookingStatus.APPROVED,
            Pageable.unpaged());

        final Pair<Map<Integer, Booking>, Map<Integer, Booking>> nearestBookings = getNearestBookings(bookings);
        final Map<Integer, Booking> lastBookings = nearestBookings.getFirst();
        final Map<Integer, Booking> nextBookings = nearestBookings.getSecond();

        final List<Comment> comments = commentRepository.findByItem_Id(
            item.getId(),
            Sort.by(Sort.Direction.ASC, "id"));

        final ItemViewDto itemViewDto = itemMapper.toItemViewDto(
            item,
            lastBookings.getOrDefault(item.getId(), null),
            nextBookings.getOrDefault(item.getId(), null),
            comments);

        return itemViewDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> search(final String text, final Pageable pageable) {
        if (text == null || text.isEmpty()) {
            return Collections.<ItemDto>emptyList();
        }
        final List<Item> items = itemRepository.search(text, pageable);
        return itemMapper.toItemDtoList(items);
    }

    @Override
    @Transactional
    public ItemDto create(final Integer userId, final ItemDto itemDto) {

        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        final Item item = itemMapper.toItem(itemDto, user);

        if (itemDto.getRequestId() != null) {
            final ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                .orElseThrow(() -> new NotFoundException(
                    String.format("Item request with id %d not found", itemDto.getRequestId())));
            item.setRequest(itemRequest);
        }

        final Item storedItem = itemRepository.save(item);
        return itemMapper.toItemDto(storedItem);
    }

    @Override
    @Transactional
    public ItemDto update(final Integer userId, final ItemDto itemDto) {

        // check if user exists
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        // check if item exists
        final Item storedItem = itemRepository.findById(itemDto.getId())
            .orElseThrow(() -> {
                throw new NotFoundException(
                    String.format("Item with id %d not found for user with id %d", itemDto.getId(), userId));
            });

        // check if user is owner of item
        if (storedItem.getOwner().getId() != userId.intValue()) {
            throw new NotFoundException(
                String.format("Item with id %d not found for user with id %d", itemDto.getId(), userId));
        }

        final Item item = itemMapper.toItem(itemDto, null);

        // update passed fields to new values
        if (item.getName() != null) {
            storedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            storedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            storedItem.setAvailable(item.getAvailable());
        }

        itemRepository.save(storedItem);
        return itemMapper.toItemDto(storedItem);
    }

    @Override
    @Transactional
    public CommentViewDto addComment(final Integer userId, final Integer itemId,
                                     final CommentCreateDto commentCreateDto) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        final Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException(String.format("Item with id %d not found", itemId)));

        final int usedCount = bookingRepository.usedCount(userId, itemId, LocalDateTime.now());
        if (usedCount == 0) {
            throw new ValidationException(String.format("Add comment for not used item"));
        }

        final Comment comment = commentMapper.toComment(commentCreateDto, user, item);

        commentRepository.save(comment);
        return commentMapper.toCommentViewDto(comment);
    }
}
