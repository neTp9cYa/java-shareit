package ru.practicum.shareit.item.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.pagination.FlexPageRequest;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentViewDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Spy
    private ItemMapper itemMapper = new ItemMapperImpl(new CommentMapperImpl());

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Spy
    private CommentMapper commentMapper = new CommentMapperImpl();

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void findByUserIdSucessfull() {
        final User user = getValidUser();
        final Item item = getValidItem();
        final Pageable pageable = FlexPageRequest.of(0, 5);

        Mockito.when(itemRepository.findByOwner_Id(anyInt(), any())).thenReturn(List.of(item));

        final List<ItemViewDto> itemViewDtos = itemService.findByUserId(user.getId(), pageable);

        assertThat(itemViewDtos, notNullValue());
        assertThat(itemViewDtos.size(), equalTo(1));
    }

    @Test
    void findByIdSucessfull() {
        final User user = getValidUser();
        final Item item = getValidItem();
        final Pageable pageable = FlexPageRequest.of(0, 5);
        final Booking booking = getValidBooking();
        final Comment comment = getValidComment();

        Mockito.when(itemRepository.findById(user.getId())).thenReturn(Optional.of(item));

        Mockito.when(bookingRepository.findByItem_Owner_IdAndStatus(anyInt(), any(), any()))
            .thenReturn(List.of(booking));

        Mockito.when(commentRepository.findByItem_Id(anyInt(), any())).thenReturn(List.of(comment));

        final ItemViewDto itemViewDto = itemService.findById(item.getOwner().getId(), item.getId());

        assertThat(itemViewDto, notNullValue());
        assertThat(itemViewDto.getId(), equalTo(item.getId()));
    }

    @Test
    void searchSuccessfull() {
        final Item item = getValidItem();

        Mockito.when(itemRepository.search(anyString(), any())).thenReturn(List.of(item));

        final List<ItemViewDto> itemViewDtos = itemService.search("text", FlexPageRequest.of(0, 5));

        assertThat(itemViewDtos, notNullValue());
        assertThat(itemViewDtos.size(), equalTo(item.getId()));
    }

    @Test
    void createSucessfull() {
        final User user = getValidUser();
        final Item item = getValidItem();
        final ItemRequest itemRequest = getValidItemRequest();
        final ItemCreateDto itemCreateDto = getValidItemCreateDto();

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Mockito.when(itemRequestRepository.findById(anyInt())).thenReturn(Optional.of(itemRequest));

        Mockito.when(itemRepository.save(any())).thenReturn(item);

        final ItemViewDto itemViewDto = itemService.create(user.getId(), itemCreateDto);

        assertThat(itemViewDto, notNullValue());
        assertThat(itemViewDto.getId(), equalTo(item.getId()));
    }

    @Test
    void updateSucessfull() {
        final User user = getValidUser();
        final Item item = getValidItem();
        final ItemRequest itemRequest = getValidItemRequest();
        final ItemUpdateDto itemUpdateDto = getValidItemUpdateDto();

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Mockito.when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));

        Mockito.when(itemRepository.save(any())).thenReturn(item);

        final ItemViewDto itemViewDto = itemService.update(user.getId(), item.getId(), itemUpdateDto);

        assertThat(itemViewDto, notNullValue());
        assertThat(itemViewDto.getId(), equalTo(item.getId()));
    }

    @Test
    void addCommentSuccessfull() {
        final User user = getValidUser();
        final Item item = getValidItem();
        final ItemRequest itemRequest = getValidItemRequest();
        final Comment comment = getValidComment();
        final CommentCreateDto commentCreateDto = getValidCommentCreateDto();

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Mockito.when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));

        Mockito.when(bookingRepository.usedCount(anyInt(), anyInt(), any())).thenReturn(1);

        Mockito.when(commentRepository.save(any())).thenReturn(comment);

        final CommentViewDto commentViewDto = itemService.addComment(user.getId(), item.getId(), commentCreateDto);

        assertThat(commentViewDto, notNullValue());
        assertThat(commentViewDto.getText(), equalTo(comment.getText()));

    }

    private User getValidUser() {
        return User.builder().id(1).name("name").email("email@email.email").build();
    }

    private Item getValidItem() {
        return Item.builder().id(1).name("name").description("email@email.email").available(true).owner(getValidUser())
            .request(null).build();
    }

    private Booking getValidBooking() {
        return Booking.builder().id(1).item(getValidItem()).booker(getValidUser())
            .start(LocalDateTime.now().plus(1, ChronoUnit.DAYS)).end(LocalDateTime.now().plus(2, ChronoUnit.DAYS))
            .status(BookingStatus.APPROVED).build();
    }

    private Comment getValidComment() {
        return Comment.builder().id(1).author(getValidUser()).item(getValidItem()).text("text_1")
            .created(LocalDateTime.now()).build();
    }

    public CommentCreateDto getValidCommentCreateDto() {
        return CommentCreateDto.builder()
            .text("text_1")
            .build();
    }

    private ItemRequest getValidItemRequest() {
        return ItemRequest.builder().id(1).owner(getValidUser()).description("description_1")
            .created(LocalDateTime.now()).build();
    }

    private ItemCreateDto getValidItemCreateDto() {
        return ItemCreateDto.builder().name("name_1").description("description_1").available(true).requestId(1).build();
    }

    private ItemUpdateDto getValidItemUpdateDto() {
        return ItemUpdateDto.builder()
            .name("name_1")
            .description("description_1")
            .available(true)
            .build();
    }
}