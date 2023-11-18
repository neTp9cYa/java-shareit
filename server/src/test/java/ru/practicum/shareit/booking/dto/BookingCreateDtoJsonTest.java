package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
class BookingCreateDtoJsonTest {

    @Autowired
    private JacksonTester<BookingCreateDto> json;

    @Test
    void testBookingCreateDto() throws Exception {
        final BookingCreateDto bookingCreateDto = BookingCreateDto.builder()
            .itemId(1)
            .start(LocalDateTime.of(2023, 11, 16, 2, 17, 31))
            .end(LocalDateTime.of(2024, 11, 16, 2, 17, 31))
            .build();

        final JsonContent<BookingCreateDto> result = json.write(bookingCreateDto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-11-16T02:17:31");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2024-11-16T02:17:31");
    }

}