package ru.practicum.shareit.item;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") final String serverUrl, final RestTemplateBuilder builder) {
        super(
            builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> addItem(final long userId,
                                          final ItemCreateDto itemCreateDto) {
        return post("", userId, itemCreateDto);
    }

    public ResponseEntity<Object> getItem(final long userId, final long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getOwnItems(final long userId,
                                              final int from,
                                              final int size) {
        Map<String, Object> parameters = Map.of(
            "from", from,
            "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> search(final long userId,
                                         final String text,
                                         final int from,
                                         final int size) {
        Map<String, Object> parameters = Map.of(
            "text", text,
            "from", from,
            "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> update(final long userId,
                                         final long itemId,
                                         final ItemUpdateDto itemUpdateDto) {
        return patch("/" + itemId, userId, null, itemUpdateDto);
    }

    public ResponseEntity<Object> addComment(final long userId,
                                             final long itemId,
                                             final CommentCreateDto commentCreateDto) {
        return post("/" + itemId + "/comment", userId, null, commentCreateDto);
    }
}
