package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
import ru.practicum.shareit.common.exception.ExceptionHelper;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentViewDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemViewDto;
import ru.practicum.shareit.item.service.ItemService;

@WebMvcTest(controllers = {ItemController.class, ExceptionHelper.class})
class ItemControllerTest {

    private final CommentCreateDto commentCreateDto1 = CommentCreateDto.builder()
        .text("text_1")
        .build();
    private final CommentViewDto commentViewDto1 = CommentViewDto.builder()
        .id(1)
        .text("text_1")
        .authorName("name_1")
        .created(LocalDateTime.now())
        .build();
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mvc;
    private ItemCreateDto itemCreateDto1 = ItemCreateDto.builder()
        .name("name_1")
        .description("description_1")
        .available(true)
        .requestId(null)
        .build();
    private ItemUpdateDto itemUpdateDto1 = ItemUpdateDto.builder()
        .name("name_1")
        .description("description_1")
        .available(true)
        .build();
    private ItemViewDto itemViewDto1 = ItemViewDto.builder()
        .id(1)
        .name("name_1")
        .description("description_1")
        .available(true)
        .build();

    @Test
    void whenRequestCorrectThenFindOwnReturnSucess() throws Exception {
        when(itemService.findByUserId(anyInt(), any()))
            .thenReturn(List.of(itemViewDto1));

        mvc.perform(get("/items")
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .param("from", "0")
                .param("size", "5"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(List.of(itemViewDto1))));
    }

    @Test
    void whenRequestCorrectThenFindByIdReturnSucess() throws Exception {
        when(itemService.findById(1, itemViewDto1.getId()))
            .thenReturn(itemViewDto1);

        mvc.perform(get("/items/" + itemViewDto1.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(itemViewDto1)));
    }

    @Test
    void whenRequestCorrectThenSearchReturnSucess() throws Exception {
        when(itemService.search(anyString(), any()))
            .thenReturn(List.of(itemViewDto1));

        mvc.perform(get("/items/search")
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .param("text", "some")
                .param("from", "0")
                .param("size", "5"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(List.of(itemViewDto1))));
    }

    @Test
    void whenRequestCorrectThenCreateReturnSucess() throws Exception {
        when(itemService.create(anyInt(), any()))
            .thenReturn(itemViewDto1);

        mvc.perform(post("/items")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(itemCreateDto1)))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(itemViewDto1)));
    }

    @Test
    void whenRequestCorrectThenUpdateReturnSucess() throws Exception {
        when(itemService.update(anyInt(), anyInt(), any()))
            .thenReturn(itemViewDto1);

        mvc.perform(patch("/items/" + itemViewDto1.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(itemUpdateDto1)))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(itemViewDto1)));
    }

    @Test
    void whenRequestCorrectThenAddCommentReturnSucess() throws Exception {
        when(itemService.addComment(anyInt(), anyInt(), any()))
            .thenReturn(commentViewDto1);

        mvc.perform(post("/items/" + itemViewDto1.getId() + "/comment")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(commentCreateDto1)))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(commentViewDto1)));
    }

}