package ru.practicum.shareit.item.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentViewDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemViewBookingDto;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
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

    @Override
    public List<ItemViewDto> findByUserId(final Integer userId) {
        final List<Item> items = itemRepository.findByUserId(userId);
        final List<ItemViewDto> itemViewDtos = new ArrayList<>();
        for (final Item item : items) {
            final ItemViewDto itemViewDto = itemMapper.toItemViewDto(item);
            populateLastNextBooking(item.getId(), itemViewDto);
            itemViewDtos.add(itemViewDto);
        }
        return itemViewDtos;
    }

    @Override
    public ItemViewDto findById(final Integer userId, final Integer itemId) {
        final Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException(String.format("Item with id %d not found", itemId)));

        final ItemViewDto itemViewDto = itemMapper.toItemViewDto(item);

        if (item.getOwner().getId().intValue() == userId.intValue()) {
            populateLastNextBooking(itemId, itemViewDto);
        }

        final List<Comment> comments = commentRepository.findByItem(item.getId());
        itemViewDto.setComments(commentMapper.toCommentViewDtoList(comments));

        return itemViewDto;
    }

    private void populateLastNextBooking(final Integer itemId, final ItemViewDto itemViewDto) {
        final List<Booking> bookings = bookingRepository.findByItem(itemId);
        final LocalDateTime now = LocalDateTime.now();
        Booking nextBooking = null;
        Booking lastBooking = null;
        for (final Booking booking : bookings) {
            if (booking.getStart().isAfter(now)) {
                if (booking.getStatus() == BookingStatus.APPROVED) {
                    nextBooking = booking;
                }
            } else {
                if (booking.getStatus() == BookingStatus.APPROVED) {
                    lastBooking = booking;
                    break;
                }
            }
        }
        if (lastBooking != null) {
            itemViewDto.setLastBooking(
                ItemViewBookingDto.builder()
                    .id(lastBooking.getId())
                    .bookerId(lastBooking.getBooker().getId())
                    .build()
            );
        }
        if (nextBooking != null) {
            itemViewDto.setNextBooking(
                ItemViewBookingDto.builder()
                    .id(nextBooking.getId())
                    .bookerId(nextBooking.getBooker().getId())
                    .build()
            );
        }
    }

    @Override
    public List<ItemDto> search(final String text) {
        if (text == null || text.isEmpty()) {
            return Collections.<ItemDto>emptyList();
        }
        final List<Item> items = itemRepository.search(text);
        return itemMapper.toItemDtoList(items);
    }

    @Override
    public ItemDto create(final Integer userId, final ItemDto itemDto) {

        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        final Item item = itemMapper.toItem(itemDto);
        item.setOwner(user);

        final Item storedItem = itemRepository.save(item);
        return itemMapper.toItemDto(storedItem);
    }

    @Override
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

        final Item item = itemMapper.toItem(itemDto);

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

        final Comment comment = commentMapper.toComment(commentCreateDto);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        commentRepository.save(comment);
        return commentMapper.toCommentViewDto(comment);
    }
}
