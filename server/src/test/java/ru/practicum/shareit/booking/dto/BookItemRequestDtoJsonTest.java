package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
class BookItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<BookItemRequestDto> json;

    @Test
    void testBookItemRequestDto() throws Exception {
        final BookItemRequestDto bookItemRequestDto = BookItemRequestDto.builder()
            .itemId(1)
            .start(LocalDateTime.of(2023, 11, 16, 2, 17, 31))
            .end(LocalDateTime.of(2024, 11, 16, 2, 17, 31))
            .build();

        final JsonContent<BookItemRequestDto> result = json.write(bookItemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-11-16T02:17:31");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2024-11-16T02:17:31");
    }

}