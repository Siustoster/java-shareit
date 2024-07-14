package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getUserItems(int userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemById(int userId, int itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> searchItem(int userId, String text) {
        if (text.isBlank()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(List.of());
        }

        Map<String, Object> parameters = Map.of("text", text);

        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> addItem(int userId, ItemDto item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> addComment(int userId, int itemId, CommentDto comment) {
        return post(String.format("/%d/comment", itemId), userId, comment);
    }

    public ResponseEntity<Object> updateItem(int userId, int itemId, ItemDto item) {
        return patch("/" + itemId, userId, item);
    }
}
