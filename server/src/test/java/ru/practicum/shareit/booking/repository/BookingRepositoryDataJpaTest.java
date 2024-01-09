package ru.practicum.shareit.booking.repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.common.pagination.FlexPageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingRepositoryDataJpaTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user2;
    private Booking booking2;
    private Booking booking4;

    @BeforeAll
    private void seed() {
        final User user1 = userRepository.save(User.builder()
            .name("name_1")
            .email("email_1")
            .build());

        user2 = userRepository.save(User.builder()
            .name("name_2")
            .email("email_2")
            .build());

        final Item item1 = itemRepository.save(Item.builder()
            .name("name_1")
            .description("description_1")
            .available(true)
            .owner(user1)
            .request(null)
            .build());

        final Item item2 = itemRepository.save(Item.builder()
            .name("name_2")
            .description("description_2")
            .available(true)
            .owner(user2)
            .request(null)
            .build());

        final Item item3 = itemRepository.save(Item.builder()
            .name("name_3")
            .description("description_3")
            .available(true)
            .owner(user2)
            .request(null)
            .build());

        final Booking booking1 = bookingRepository.save(Booking.builder()
            .item(item1)
            .booker(user1)
            .start(LocalDateTime.now().minus(2, ChronoUnit.DAYS))
            .end(LocalDateTime.now().plus(2, ChronoUnit.DAYS))
            .status(BookingStatus.APPROVED)
            .build());

        booking2 = bookingRepository.save(Booking.builder()
            .item(item2)
            .booker(user2)
            .start(LocalDateTime.now().minus(2, ChronoUnit.DAYS))
            .end(LocalDateTime.now().plus(2, ChronoUnit.DAYS))
            .status(BookingStatus.APPROVED)
            .build());

        final Booking booking3 = bookingRepository.save(Booking.builder()
            .item(item3)
            .booker(user2)
            .start(LocalDateTime.now().minus(5, ChronoUnit.DAYS))
            .end(LocalDateTime.now().minus(4, ChronoUnit.DAYS))
            .status(BookingStatus.APPROVED)
            .build());

        booking4 = bookingRepository.save(Booking.builder()
            .item(item3)
            .booker(user2)
            .start(LocalDateTime.now().minus(1, ChronoUnit.DAYS))
            .end(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
            .status(BookingStatus.APPROVED)
            .build());

        final Booking booking5 = bookingRepository.save(Booking.builder()
            .item(item3)
            .booker(user2)
            .start(LocalDateTime.now().plus(4, ChronoUnit.DAYS))
            .end(LocalDateTime.now().plus(5, ChronoUnit.DAYS))
            .status(BookingStatus.APPROVED)
            .build());
    }

    @Test
    void verifyBootstrappingByPersistingAnEmployee() {
        final List<Booking> bookings = bookingRepository.findOwnCurrent(
            user2.getId(), LocalDateTime.now(), FlexPageRequest.of(0, 10));

        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(2));
        assertThat(bookings.get(0).getId(), equalTo(booking4.getId()));
        assertThat(bookings.get(1).getId(), equalTo(booking2.getId()));
    }
}