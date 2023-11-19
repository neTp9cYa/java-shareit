package ru.practicum.shareit.request;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") final String serverUrl,
                             final RestTemplateBuilder builder) {
        super(
            builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> addItemRequest(final long userId,
                                                 final ItemRequestCreateDto itemRequestCreateDto) {
        return post("", userId, itemRequestCreateDto);
    }

    public ResponseEntity<Object> getItemRequest(final long userId, final long itemRequestId) {
        return get("/" + itemRequestId, userId);
    }

    public ResponseEntity<Object> getOwnItemRequests(final long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemRequests(final long userId,
                                                  final int from,
                                                  final int size) {
        Map<String, Object> parameters = Map.of(
            "from", from,
            "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }
}
