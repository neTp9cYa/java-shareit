package ru.practicum.shareit.request.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.common.pagination.FlexPageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.CommentMapperImpl;
import ru.practicum.shareit.item.service.ItemMapperImpl;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestViewDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Spy
    private ItemRequestMapper itemRequestMapper = new ItemRequestMapperImpl(
        new ItemMapperImpl(new CommentMapperImpl()));

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void createSuccessfull() {
        final User user = getValidUser();
        final ItemRequest itemRequest = getValidItemRequest();
        final ItemRequestCreateDto itemRequestCreateDto = getValidItemRequestCreateDto();

        Mockito
            .when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        Mockito
            .when(itemRequestRepository.save(any()))
            .thenReturn(itemRequest);

        final ItemRequestViewDto itemRequestViewDto = itemRequestService.create(
            user.getId(),
            itemRequestCreateDto);

        assertThat(itemRequestViewDto, notNullValue());
        assertThat(itemRequestViewDto.getId(), equalTo(itemRequest.getId()));
    }

    @Test
    void findOwnSuccessfull() {
        final User user = getValidUser();
        final ItemRequest itemRequest = getValidItemRequest();
        final ItemRequestCreateDto itemRequestCreateDto = getValidItemRequestCreateDto();
        final Item item = getValidItem();

        Mockito
            .when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        Mockito
            .when(itemRequestRepository.findByOwner_Id(anyInt()))
            .thenReturn(List.of(itemRequest));

        Mockito
            .when(itemRepository.findByRequest_Owner_Id(anyInt()))
            .thenReturn(List.of(item));

        final List<ItemRequestViewDto> itemRequestViewDtos = itemRequestService.findOwn(user.getId());

        assertThat(itemRequestViewDtos, notNullValue());
        assertThat(itemRequestViewDtos.size(), equalTo(1));
    }

    @Test
    void findSomeoneElsesSuccessfull() {
        final User user = getValidUser();
        final ItemRequest itemRequest = getValidItemRequest();
        final ItemRequestCreateDto itemRequestCreateDto = getValidItemRequestCreateDto();
        final Item item = getValidItem();
        final Pageable pageable = FlexPageRequest.of(0, 5);

        Mockito
            .when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        Mockito
            .when(itemRequestRepository.findByOwner_IdNot(anyInt(), any()))
            .thenReturn(List.of(itemRequest));

        Mockito
            .when(itemRepository.findByRequest_Owner_IdNot(anyInt()))
            .thenReturn(List.of(item));

        final List<ItemRequestViewDto> itemRequestViewDtos = itemRequestService.findSomeoneElses(
            user.getId(),
            pageable);

        assertThat(itemRequestViewDtos, notNullValue());
        assertThat(itemRequestViewDtos.size(), equalTo(1));
    }

    @Test
    void findByIdSuccessfull() {
        final User user = getValidUser();
        final ItemRequest itemRequest = getValidItemRequest();
        final ItemRequestCreateDto itemRequestCreateDto = getValidItemRequestCreateDto();
        final Item item = getValidItem();
        final Pageable pageable = FlexPageRequest.of(0, 5);

        Mockito
            .when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));

        Mockito
            .when(itemRequestRepository.findById(itemRequest.getId()))
            .thenReturn(Optional.of(itemRequest));

        Mockito
            .when(itemRepository.findByRequest_Id(itemRequest.getId()))
            .thenReturn(List.of(item));

        final ItemRequestViewDto itemRequestViewDto = itemRequestService.findById(user.getId(), itemRequest.getId());

        assertThat(itemRequestViewDto, notNullValue());
        assertThat(itemRequestViewDto.getId(), equalTo(itemRequest.getId()));
    }

    private User getValidUser() {
        return User.builder()
            .id(1)
            .name("name")
            .email("email@email.email")
            .build();
    }

    private ItemRequest getValidItemRequest() {
        return ItemRequest.builder()
            .id(1)
            .owner(null)
            .description("description_1")
            .created(LocalDateTime.now())
            .build();
    }

    private ItemRequestCreateDto getValidItemRequestCreateDto() {
        return ItemRequestCreateDto.builder()
            .description("description_1")
            .build();
    }

    private Item getValidItem() {
        return Item.builder()
            .id(1)
            .name("name_1")
            .description("description_1")
            .available(true)
            .owner(getValidUser())
            .request(getValidItemRequest())
            .build();
    }
}