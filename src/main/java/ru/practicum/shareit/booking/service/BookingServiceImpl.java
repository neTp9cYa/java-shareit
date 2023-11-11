package ru.practicum.shareit.booking.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingStatusDto;
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
    public BookingViewDto create(final Integer userId, final BookingCreateDto bookingCreateDto) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        final Item item = itemRepository.findById(bookingCreateDto.getItemId())
            .orElseThrow(
                () -> new NotFoundException(String.format("Item with id %d not found", bookingCreateDto.getItemId())));

        if (item.getOwner().getId() == userId) {
            throw new NotFoundException(String.format("Item with id %d not found", bookingCreateDto.getItemId()));
        }

        if (!item.getAvailable()) {
            throw new ValidationException(String.format("Item with id %d is not available", item.getId()));
        }

        final LocalDateTime now = LocalDateTime.now();
        if (bookingCreateDto.getStart().isBefore(now)) {
            throw new ValidationException("Start date is in past");
        }
        if (bookingCreateDto.getEnd().isBefore(now)) {
            throw new ValidationException("End date is in past");
        }
        if (bookingCreateDto.getEnd().isEqual(bookingCreateDto.getStart())) {
            throw new ValidationException("End date equals start date");
        }
        if (bookingCreateDto.getEnd().isBefore(bookingCreateDto.getStart())) {
            throw new ValidationException("End date is earlier than start date");
        }

        final Booking booking = bookingMapper.toBooking(bookingCreateDto);
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(user);
        booking.setItem(item);

        final Booking storedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingViewDto(storedBooking);
    }

    @Override
    public BookingViewDto approveOrReject(final Integer userId, final Integer bookingId, final Boolean approved) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        final Booking storedBooking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new NotFoundException(
                String.format("Booking with id %d not found", bookingId)));

        if (storedBooking.getItem().getOwner().getId() != userId) {
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
    public BookingViewDto findById(Integer userId, Integer bookingId) {
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        final Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new NotFoundException(
                String.format("Booking with id %d not found", bookingId)));

        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new NotFoundException(
                String.format("Booking with id %d not found", bookingId));
        }

        return bookingMapper.toBookingViewDto(booking);
    }

    @Override
    public List<BookingViewDto> findOwn(final Integer userId, final BookingStatusDto bookingStatusDto) {
        userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        List<Booking> bookings = null;

        if (bookingStatusDto == BookingStatusDto.ALL) {
            bookings = bookingRepository.findOwn(userId);
        } else if (bookingStatusDto == BookingStatusDto.WAITING) {
            bookings = bookingRepository.findOwn(userId, BookingStatus.WAITING);
        } else if (bookingStatusDto == BookingStatusDto.REJECTED) {
            bookings = bookingRepository.findOwn(userId, BookingStatus.REJECTED);
        } else if (bookingStatusDto == BookingStatusDto.PAST) {
            bookings = bookingRepository.findOwnInPast(userId, LocalDateTime.now());
        } else if (bookingStatusDto == BookingStatusDto.CURRENT) {
            bookings = bookingRepository.findOwnCurrent(userId, LocalDateTime.now());
        } else if (bookingStatusDto == BookingStatusDto.FUTURE) {
            bookings = bookingRepository.findOwnInFuture(userId, LocalDateTime.now());
        } else {
            throw new RuntimeException("Not supported");
        }

        return bookingMapper.toBookingViewDtoList(bookings);
    }

    @Override
    public List<BookingViewDto> findByItemOwner(final Integer userId, final BookingStatusDto bookingStatusDto) {
        userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        List<Booking> bookings = null;

        if (bookingStatusDto == BookingStatusDto.ALL) {
            bookings = bookingRepository.findByItemOwner(userId);
        } else if (bookingStatusDto == BookingStatusDto.WAITING) {
            bookings = bookingRepository.findByItemOwner(userId, BookingStatus.WAITING);
        } else if (bookingStatusDto == BookingStatusDto.REJECTED) {
            bookings = bookingRepository.findByItemOwner(userId, BookingStatus.REJECTED);
        } else if (bookingStatusDto == BookingStatusDto.PAST) {
            bookings = bookingRepository.findByItemOwnerInPast(userId, LocalDateTime.now());
        } else if (bookingStatusDto == BookingStatusDto.CURRENT) {
            bookings = bookingRepository.findByItemOwnerCurrent(userId, LocalDateTime.now());
        } else if (bookingStatusDto == BookingStatusDto.FUTURE) {
            bookings = bookingRepository.findByItemOwnerInFuture(userId, LocalDateTime.now());
        } else {
            throw new RuntimeException("Not supported");
        }

        return bookingMapper.toBookingViewDtoList(bookings);
    }
}
