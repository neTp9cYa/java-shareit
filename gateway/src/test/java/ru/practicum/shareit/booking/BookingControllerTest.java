package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.common.exception.ExceptionHelper;

@WebMvcTest(controllers = {BookingController.class, ExceptionHelper.class})
class BookingControllerTest {

    private final BookItemRequestDto bookItemRequestDto1 = BookItemRequestDto.builder()
        .itemId(1)
        .start(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
        .end(LocalDateTime.now().plus(2, ChronoUnit.DAYS))
        .build();
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingClient bookingClient;

    @Test
    void whenEndBeforeStartThenCreateReturn400() throws Exception {
        mvc.perform(post("/bookings")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(
                    BookItemRequestDto.builder()
                        .itemId(1)
                        .start(LocalDateTime.now().plus(2, ChronoUnit.DAYS))
                        .end(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                        .build()
                )))
            .andExpect(status().isBadRequest());
    }
}