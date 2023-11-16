package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import ru.practicum.shareit.common.exception.ExceptionHelper;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestViewDto;
import ru.practicum.shareit.request.service.ItemRequestService;

@WebMvcTest(controllers = {ItemRequestController.class, ExceptionHelper.class})
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private ItemRequestCreateDto itemRequestCreateDto1 = ItemRequestCreateDto.builder()
        .description("description_1")
        .build();
    private ItemRequestViewDto itemRequestViewDto1 = ItemRequestViewDto.builder()
        .id(1)
        .description("description_1")
        .created(LocalDateTime.now().minus(2, ChronoUnit.DAYS))
        .item(ItemViewDto.builder()
            .id(1)
            .name("name_1")
            .description("description_1")
            .available(true)
            .requestId(1)
            .build())
        .build();
    private ItemRequestViewDto itemRequestViewDto2 = ItemRequestViewDto.builder()
        .id(2)
        .description("description_2")
        .created(LocalDateTime.now().minus(1, ChronoUnit.DAYS))
        .item(ItemViewDto.builder()
            .id(1)
            .name("name_2")
            .description("description_2")
            .available(true)
            .requestId(2)
            .build())
        .item(ItemViewDto.builder()
            .id(1)
            .name("name_3")
            .description("description_3")
            .available(false)
            .requestId(3)
            .build())
        .build();

    @Test
    void whenRequestCorrectThenCreateReturnSucess() throws Exception {
        when(itemRequestService.create(anyInt(), any()))
            .thenReturn(itemRequestViewDto1);

        mvc.perform(post("/requests")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(itemRequestCreateDto1)))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(itemRequestViewDto1)));
    }

    @Test
    void whenRequestCorrectThenFindOwnReturnSucess() throws Exception {
        when(itemRequestService.findOwn(1))
            .thenReturn(List.of(itemRequestViewDto1, itemRequestViewDto2));

        mvc.perform(get("/requests")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .param("from", "0")
                .param("size", "5"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestViewDto1, itemRequestViewDto2))));
    }

    @Test
    void whenRequestCorrectThenFindSomeoneElsesReturnSucess() throws Exception {
        when(itemRequestService.findSomeoneElses(anyInt(), any()))
            .thenReturn(List.of(itemRequestViewDto1, itemRequestViewDto2));

        mvc.perform(get("/requests/all")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .param("from", "0")
                .param("size", "5"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestViewDto1, itemRequestViewDto2))));
    }

    @Test
    void whenRequestCorrectThenFindByIdReturnSucess() throws Exception {
        when(itemRequestService.findById(1, itemRequestViewDto2.getId()))
            .thenReturn(itemRequestViewDto2);

        mvc.perform(get("/requests/" + itemRequestViewDto2.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(itemRequestViewDto2)));
    }
}