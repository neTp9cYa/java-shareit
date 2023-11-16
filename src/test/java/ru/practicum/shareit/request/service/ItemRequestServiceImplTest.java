package ru.practicum.shareit.request.service;

import java.time.LocalDateTime;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
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
}