package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String SHARER = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(SHARER) int userId) {
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(SHARER) int userId, @PathVariable int itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(SHARER) int userId, @RequestParam String text) {
        return itemClient.searchItem(userId, text);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(SHARER) int userId,
                                          @RequestBody @Valid ItemDto itemCreateUpdateDto) {
        return itemClient.addItem(userId, itemCreateUpdateDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(SHARER) int userId,
                                             @PathVariable int itemId,
                                             @RequestBody @Valid CommentDto commentRequest) {
        return itemClient.addComment(userId, itemId, commentRequest);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(SHARER) int userId,
                                             @PathVariable int itemId,
                                             @RequestBody ItemDto itemCreateUpdateDto) {
        return itemClient.updateItem(userId, itemId, itemCreateUpdateDto);
    }
}
