package ru.practicum.shareit.booking.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.common.pagination.FlexPageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.CommentMapperImpl;
import ru.practicum.shareit.item.service.ItemMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserMapperImpl;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplUnitTest {

    @Mock
    private BookingRepository bookingRepository;

    @Spy
    private BookingMapperImpl bookingMapper = new BookingMapperImpl(
        new ItemMapperImpl(new CommentMapperImpl()), new UserMapperImpl());

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void whenCreateWithAbsentUserThenThrowException() {
        final User user = getValidUser();

        Mockito
            .when(userRepository.findById(Mockito.anyInt()))
            .thenReturn(Optional.empty());

        final NotFoundException exception = Assertions.assertThrows(
            NotFoundException.class,
            () -> bookingService.create(user.getId(), getValidBookingCreateDto()));

        Assertions.assertEquals(
            String.format("User with id %d not found", user.getId()),
            exception.getMessage());
    }

    @Test
    void whenCreateWithAbsentItemThenThrowException() {
        final BookingCreateDto bookingCreateDto = getValidBookingCreateDto();

        Mockito
            .when(userRepository.findById(Mockito.anyInt()))
            .thenReturn(Optional.of(getValidUser()));

        Mockito
            .when(itemRepository.findById(Mockito.anyInt()))
            .thenReturn(Optional.empty());

        final NotFoundException exception = Assertions.assertThrows(
            NotFoundException.class,
            () -> bookingService.create(1, bookingCreateDto));

        Assertions.assertEquals(
            String.format("Item with id %d not found", bookingCreateDto.getItemId()),
            exception.getMessage());
    }

    @Test
    void whenCreateForOwnItemThenThrowException() {
        final Item item = getValidItem();

        Mockito
            .when(userRepository.findById(Mockito.anyInt()))
            .thenReturn(Optional.of(item.getOwner()));

        Mockito
            .when(itemRepository.findById(Mockito.anyInt()))
            .thenReturn(Optional.of(item));

        final NotFoundException exception = Assertions.assertThrows(
            NotFoundException.class,
            () -> bookingService.create(item.getOwner().getId(), getValidBookingCreateDto()));

        Assertions.assertEquals(
            String.format("Item with id %d not found", item.getId()),
            exception.getMessage());
    }

    @Test
    void whenCreateForUnavaliableItemThenThrowException() {
        final User user = getValidUser();
        final Item item = getValidItem();
        item.getOwner().setId(user.getId() + 1);
        item.setAvailable(false);

        Mockito
            .when(userRepository.findById(Mockito.anyInt()))
            .thenReturn(Optional.of(user));

        Mockito
            .when(itemRepository.findById(Mockito.anyInt()))
            .thenReturn(Optional.of(item));

        final ValidationException exception = Assertions.assertThrows(
            ValidationException.class,
            () -> bookingService.create(item.getOwner().getId(), getValidBookingCreateDto()));

        Assertions.assertEquals(
            String.format("Item with id %d is not available", item.getId()),
            exception.getMessage());
    }

    @Test
    void whenCreateWithValidDataThenSuccess() {

        final BookingCreateDto bookingCreateDto = getValidBookingCreateDto();
        final User user = getValidUser();
        final Item item = getValidItem();
        item.getOwner().setId(user.getId() + 1);
        final Booking booking = getValidBooking();

        Mockito
            .when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        Mockito
            .when(itemRepository.findById(item.getId()))
            .thenReturn(Optional.of(item));

        Mockito
            .when(bookingRepository.save(any()))
            .thenReturn(booking);

        bookingService.create(user.getId(), bookingCreateDto);

        Mockito.verify(userRepository, Mockito.times(1))
            .findById(user.getId());

        Mockito.verify(itemRepository, Mockito.times(1))
            .findById(item.getId());

        Mockito.verify(bookingRepository, Mockito.times(1))
            .save(any());

        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository);
    }

    @Test
    void whenApproveNotInWaitingStatusThenThrow() {
        final User user = getValidUser();
        final Booking booking = getValidBooking();
        booking.setStatus(BookingStatus.APPROVED);

        Mockito
            .when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        Mockito
            .when(bookingRepository.findById(booking.getId()))
            .thenReturn(Optional.of(booking));

        final ValidationException exception = Assertions.assertThrows(
            ValidationException.class,
            () -> bookingService.approveOrReject(user.getId(), booking.getId(), true));

        Assertions.assertEquals(
            String.format("Status in final state"),
            exception.getMessage());
    }

    @Test
    void whenApproveInWaitingStatusThenSuccess() {
        final User user = getValidUser();
        final Booking booking = getValidBooking();
        booking.setStatus(BookingStatus.WAITING);

        Mockito
            .when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        Mockito
            .when(bookingRepository.findById(booking.getId()))
            .thenReturn(Optional.of(booking));

        bookingService.approveOrReject(user.getId(), booking.getId(), true);

        Mockito.verify(userRepository, Mockito.times(1))
            .findById(user.getId());

        Mockito.verify(bookingRepository, Mockito.times(1))
            .findById(booking.getId());

        Mockito.verify(bookingRepository, Mockito.times(1))
            .save(booking);

        Mockito.verifyNoMoreInteractions(userRepository, itemRepository, bookingRepository);
    }

    @Test
    void whenFindByIdSomeoneElsesBookingThenThrow() {
        final User user = getValidUser();
        user.setId(1);

        final Booking booking = getValidBooking();
        booking.getBooker().setId(2);
        booking.getItem().getOwner().setId(3);

        Mockito
            .when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        Mockito
            .when(bookingRepository.findById(booking.getId()))
            .thenReturn(Optional.of(booking));

        final NotFoundException exception = Assertions.assertThrows(
            NotFoundException.class,
            () -> bookingService.findById(user.getId(), booking.getId()));

        Assertions.assertEquals(
            String.format("Booking with id %d not found", booking.getId()),
            exception.getMessage());
    }

    @Test
    void whenFindByIdOwnBookingThenSuccess() {
        final User user = getValidUser();
        user.setId(1);

        final Booking booking = getValidBooking();
        booking.setBooker(user);
        booking.getItem().getOwner().setId(3);

        Mockito
            .when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        Mockito
            .when(bookingRepository.findById(booking.getId()))
            .thenReturn(Optional.of(booking));

        bookingService.findById(user.getId(), booking.getId());
    }

    @Test
    void whenFindOwnThenSuccess() {
        final User user = getValidUser();
        final Pageable pageable = FlexPageRequest.of(0, 5);

        Mockito
            .when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        bookingService.findOwn(user.getId(), BookingState.ALL, pageable);

        Mockito.verify(bookingRepository, Mockito.times(1))
            .findOwn(user.getId(), pageable);

        Mockito.verifyNoMoreInteractions(bookingRepository);
    }

    private BookingCreateDto getValidBookingCreateDto() {
        return BookingCreateDto.builder()
            .itemId(1)
            .start(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
            .end(LocalDateTime.now().plus(2, ChronoUnit.DAYS))
            .build();
    }

    private User getValidUser() {
        return User.builder()
            .id(1)
            .name("name")
            .email("email@email.email")
            .build();
    }

    private Item getValidItem() {
        return Item.builder()
            .id(1)
            .name("name")
            .description("email@email.email")
            .available(true)
            .owner(getValidUser())
            .request(null)
            .build();
    }

    private Booking getValidBooking() {
        return Booking.builder()
            .id(1)
            .item(getValidItem())
            .booker(getValidUser())
            .start(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
            .end(LocalDateTime.now().plus(2, ChronoUnit.DAYS))
            .status(BookingStatus.APPROVED)
            .build();
    }
}