package ru.practicum.shareit.item.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.common.pagination.FlexPageRequest;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplIntegrationTest {

    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserRepository userRepository;

    @Test
    void findByUserId() {
        final User user1 = getValidUser1();
        final User user2 = getValidUser2();

        userRepository.save(user1);
        userRepository.save(user2);

        final ItemCreateDto itemCreateDto1 = getItemCreateDto1();
        final ItemCreateDto itemCreateDto2 = getItemCreateDto2();
        final ItemCreateDto itemCreateDto3 = getItemCreateDto3();

        itemService.create(user1.getId(), itemCreateDto1);
        itemService.create(user2.getId(), itemCreateDto2);
        itemService.create(user2.getId(), itemCreateDto3);

        final List<ItemViewDto> itemsOfUser1 = itemService.findByUserId(
            user1.getId(),
            FlexPageRequest.of(0, 10));
        final List<ItemViewDto> itemsOfUser2 = itemService.findByUserId(
            user2.getId(),
            FlexPageRequest.of(0, 10));

        assertThat(itemsOfUser1, notNullValue());
        assertThat(itemsOfUser1.size(), equalTo(1));
        assertThat(itemsOfUser1.get(0).getName(), equalTo(itemCreateDto1.getName()));
        assertThat(itemsOfUser1.get(0).getDescription(), equalTo(itemCreateDto1.getDescription()));
        assertThat(itemsOfUser1.get(0).getAvailable(), equalTo(itemCreateDto1.getAvailable()));

        assertThat(itemsOfUser2, notNullValue());
        assertThat(itemsOfUser2.size(), equalTo(2));
        assertThat(itemsOfUser2.get(0).getName(), equalTo(itemCreateDto2.getName()));
        assertThat(itemsOfUser2.get(0).getDescription(), equalTo(itemCreateDto2.getDescription()));
        assertThat(itemsOfUser2.get(0).getAvailable(), equalTo(itemCreateDto2.getAvailable()));
        assertThat(itemsOfUser2.get(1).getName(), equalTo(itemCreateDto3.getName()));
        assertThat(itemsOfUser2.get(1).getDescription(), equalTo(itemCreateDto3.getDescription()));
        assertThat(itemsOfUser2.get(1).getAvailable(), equalTo(itemCreateDto3.getAvailable()));
    }

    private User getValidUser1() {
        return User.builder()
            .id(1)
            .name("name_1")
            .email("email_1@email.email")
            .build();
    }

    private User getValidUser2() {
        return User.builder()
            .id(2)
            .name("name_2")
            .email("email_2@email.email")
            .build();
    }

    private ItemCreateDto getItemCreateDto1() {
        return ItemCreateDto.builder()
            .name("name_1")
            .description("description_1")
            .available(true)
            .requestId(null)
            .build();
    }

    private ItemCreateDto getItemCreateDto2() {
        return ItemCreateDto.builder()
            .name("name_1")
            .description("description_1")
            .available(false)
            .requestId(null)
            .build();
    }

    private ItemCreateDto getItemCreateDto3() {
        return ItemCreateDto.builder()
            .name("name_1")
            .description("description_1")
            .available(true)
            .requestId(null)
            .build();
    }
}