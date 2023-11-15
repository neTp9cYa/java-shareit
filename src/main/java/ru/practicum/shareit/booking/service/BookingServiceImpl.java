package ru.practicum.shareit.booking.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingViewDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingViewDto create(final int userId, final BookingCreateDto bookingCreateDto) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        final Item item = itemRepository.findById(bookingCreateDto.getItemId())
            .orElseThrow(
                () -> new NotFoundException(String.format("Item with id %d not found", bookingCreateDto.getItemId())));

        validateBookingCreation(user, item);

        final Booking booking = bookingMapper.toBooking(bookingCreateDto, user, item);

        final Booking storedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingViewDto(storedBooking);
    }

    private void validateBookingCreation(final User user, final Item item) throws ValidationException {
        if (item.getOwner().getId().intValue() == user.getId().intValue()) {
            throw new NotFoundException(String.format("Item with id %d not found", item.getId()));
        }

        if (!item.getAvailable()) {
            throw new ValidationException(String.format("Item with id %d is not available", item.getId()));
        }
    }

    @Override
    @Transactional
    public BookingViewDto approveOrReject(final int userId, final Integer bookingId, final Boolean approved) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        final Booking storedBooking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new NotFoundException(
                String.format("Booking with id %d not found", bookingId)));

        if (storedBooking.getItem().getOwner().getId().intValue() != userId) {
            throw new NotFoundException(
                String.format("Booking with id %d not found", bookingId));
        }

        if (storedBooking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException(String.format("Status in final state"));
        }

        storedBooking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        bookingRepository.save(storedBooking);
        return bookingMapper.toBookingViewDto(storedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingViewDto findById(final int userId, final Integer bookingId) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        final Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new NotFoundException(
                String.format("Booking with id %d not found", bookingId)));

        if (booking.getItem().getOwner().getId().intValue() != userId &&
            booking.getBooker().getId().intValue() != userId) {
            throw new NotFoundException(
                String.format("Booking with id %d not found", bookingId));
        }

        return bookingMapper.toBookingViewDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingViewDto> findOwn(final int userId,
                                        final BookingState bookingStatusDto,
                                        final Pageable pageable) {
        userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        List<Booking> bookings = null;

        if (bookingStatusDto == BookingState.ALL) {
            bookings = bookingRepository.findOwn(userId, pageable);
        } else if (bookingStatusDto == BookingState.WAITING) {
            bookings = bookingRepository.findOwn(userId, BookingStatus.WAITING, pageable);
        } else if (bookingStatusDto == BookingState.REJECTED) {
            bookings = bookingRepository.findOwn(userId, BookingStatus.REJECTED, pageable);
        } else if (bookingStatusDto == BookingState.PAST) {
            bookings = bookingRepository.findOwnInPast(userId, LocalDateTime.now(), pageable);
        } else if (bookingStatusDto == BookingState.CURRENT) {
            bookings = bookingRepository.findOwnCurrent(userId, LocalDateTime.now(), pageable);
        } else if (bookingStatusDto == BookingState.FUTURE) {
            bookings = bookingRepository.findOwnInFuture(userId, LocalDateTime.now(), pageable);
        } else {
            throw new RuntimeException("Not supported");
        }

        return bookingMapper.toBookingViewDtoList(bookings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingViewDto> findByItemOwner(final int userId,
                                                final BookingState bookingStatusDto,
                                                final Pageable pageable) {
        userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        List<Booking> bookings = null;

        if (bookingStatusDto == BookingState.ALL) {
            bookings = bookingRepository.findByItemOwner(userId, pageable);
        } else if (bookingStatusDto == BookingState.WAITING) {
            bookings = bookingRepository.findByItemOwner(userId, BookingStatus.WAITING, pageable);
        } else if (bookingStatusDto == BookingState.REJECTED) {
            bookings = bookingRepository.findByItemOwner(userId, BookingStatus.REJECTED, pageable);
        } else if (bookingStatusDto == BookingState.PAST) {
            bookings = bookingRepository.findByItemOwnerInPast(userId, LocalDateTime.now(), pageable);
        } else if (bookingStatusDto == BookingState.CURRENT) {
            bookings = bookingRepository.findByItemOwnerCurrent(userId, LocalDateTime.now(), pageable);
        } else if (bookingStatusDto == BookingState.FUTURE) {
            bookings = bookingRepository.findByItemOwnerInFuture(userId, LocalDateTime.now(), pageable);
        } else {
            throw new RuntimeException("Not supported");
        }

        return bookingMapper.toBookingViewDtoList(bookings);
    }
}
