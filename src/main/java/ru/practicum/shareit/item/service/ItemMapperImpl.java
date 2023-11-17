package ru.practicum.shareit.item.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemViewBookingDto;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor
public class ItemMapperImpl implements ItemMapper {

    private final CommentMapper commentMapper;

    @Override
    public ItemViewDto toItemViewDto(final Item item,
                                     final Booking lastBooking,
                                     final Booking nextBooking) {
        return toItemViewDto(item, lastBooking, nextBooking, null);
    }

    @Override
    public ItemViewDto toItemViewDto(final Item item,
                                     final Booking lastBooking,
                                     final Booking nextBooking,
                                     final List<Comment> comments) {
        return ItemViewDto.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .available(item.getAvailable())
            .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
            .lastBooking(toItemViewBookingDto(lastBooking))
            .nextBooking(toItemViewBookingDto(nextBooking))
            .comments(commentMapper.toCommentViewDtoList(comments))
            .build();
    }

    private ItemViewBookingDto toItemViewBookingDto(final Booking booking) {
        if (booking == null) {
            return null;
        }

        return ItemViewBookingDto.builder()
            .id(booking.getId())
            .bookerId(booking.getBooker().getId())
            .build();
    }

    @Override
    public List<ItemViewDto> toItemViewDtoList(final List<Item> items) {
        return items.stream()
            .map(item -> toItemViewDto(item, null, null))
            .collect(Collectors.toList());
    }

    @Override
    public Item toItem(final ItemUpdateDto itemUpdateDto, final User owner) {
        return Item.builder()
            .name(itemUpdateDto.getName())
            .description(itemUpdateDto.getDescription())
            .available(itemUpdateDto.getAvailable())
            .owner(owner)
            .build();
    }

    @Override
    public Item toItem(final ItemCreateDto itemCreateDto, final User owner) {
        return Item.builder()
            .name(itemCreateDto.getName())
            .description(itemCreateDto.getDescription())
            .available(itemCreateDto.getAvailable())
            .owner(owner)
            .build();
    }
}
