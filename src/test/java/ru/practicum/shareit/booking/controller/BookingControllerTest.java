package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingViewDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.exception.ExceptionHelper;

@WebMvcTest(controllers = {BookingController.class, ExceptionHelper.class})
class BookingControllerTest {

    private final BookingCreateDto bookingCreateDto1 = BookingCreateDto.builder()
        .itemId(1)
        .start(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
        .end(LocalDateTime.now().plus(2, ChronoUnit.DAYS))
        .build();
    private final BookingViewDto bookingViewDto1 = BookingViewDto.builder()
        .id(1)
        .item(null)
        .booker(null)
        .start(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
        .end(LocalDateTime.now().plus(2, ChronoUnit.DAYS))
        .status(BookingStatus.WAITING)
        .build();
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;

    @Test
    void whenRequestCorrectThenCreateReturnSucess() throws Exception {
        when(bookingService.create(anyInt(), any()))
            .thenReturn(bookingViewDto1);

        mvc.perform(post("/bookings")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(bookingCreateDto1)))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(bookingViewDto1)));
    }

    @Test
    void whenRequestCorrectThenApproveOrRejectReturnSucess() throws Exception {
        when(bookingService.approveOrReject(anyInt(), anyInt(), anyBoolean()))
            .thenReturn(bookingViewDto1);

        mvc.perform(patch("/bookings/" + bookingViewDto1.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .param("approved", "true"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(bookingViewDto1)));
    }

    @Test
    void whenRequestCorrectThenFindByIdReturnSucess() throws Exception {
        when(bookingService.findById(anyInt(), anyInt()))
            .thenReturn(bookingViewDto1);

        mvc.perform(get("/bookings/" + bookingViewDto1.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(bookingViewDto1)));
    }

    @Test
    void whenRequestCorrectThenFindOwnReturnSucess() throws Exception {
        when(bookingService.findOwn(anyInt(), any(), any()))
            .thenReturn(List.of(bookingViewDto1));

        mvc.perform(get("/bookings")
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .param("state", BookingState.CURRENT.toString())
                .param("from", "0")
                .param("size", "5"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(List.of(bookingViewDto1))));
    }

    @Test
    void whenRequestCorrectThenFindByItemOwnerReturnSucess() throws Exception {
        when(bookingService.findByItemOwner(anyInt(), any(), any()))
            .thenReturn(List.of(bookingViewDto1));

        mvc.perform(get("/bookings/owner")
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .param("state", BookingState.CURRENT.toString())
                .param("from", "0")
                .param("size", "5"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(List.of(bookingViewDto1))));
    }

}