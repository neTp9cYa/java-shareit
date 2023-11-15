package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.hamcrest.Matchers.is;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import ru.practicum.shareit.common.exception.ExceptionHelper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestViewDto;
import ru.practicum.shareit.request.service.ItemRequestService;

@WebMvcTest(controllers = {ItemRequestController.class, ExceptionHelper.class})
class ItemRequestControllerWebMvcTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;
    private ItemRequestViewDto itemRequestViewDto1 = ItemRequestViewDto.builder()
        .id(1)
        .description("description_1")
        .created(LocalDateTime.now().minus(2, ChronoUnit.DAYS))
        .item(ItemDto.builder()
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
        .item(ItemDto.builder()
            .id(1)
            .name("name_2")
            .description("description_2")
            .available(true)
            .requestId(2)
            .build())
        .item(ItemDto.builder()
            .id(1)
            .name("name_3")
            .description("description_3")
            .available(false)
            .requestId(3)
            .build())
        .build();

    @Test
    void findSomeoneElses() throws Exception {
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
            .andExpect(jsonPath("$[0].id", is(itemRequestViewDto1.getId()), Integer.class))
            .andExpect(jsonPath("$[0].description", is(itemRequestViewDto1.getDescription())))
            .andExpect(jsonPath("$[0].items[0].id", is(itemDto1().getId()), Integer.class))
            .andExpect(jsonPath("$[0].items[0].name", is(itemDto1().getName())))
            .andExpect(jsonPath("$[0].items[0].description", is(itemDto1().getDescription())))
            .andExpect(jsonPath("$[0].items[0].available", is(itemDto1().getAvailable()), Boolean.class))
            .andExpect(jsonPath("$[0].items[0].requestId", is(itemDto1().getRequestId()), Integer.class))
            .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestViewDto1, itemRequestViewDto2))));
    }

    private ItemDto itemDto1() {
        return itemRequestViewDto1.getItems().get(0);
    }
}