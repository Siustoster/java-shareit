package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String header = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createItem(@RequestHeader(header) int userId, @RequestBody @Valid ItemDto item) {
        return itemService.createItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(header) int userId, @PathVariable int itemId,
                              @RequestBody ItemDto item) {
        return itemService.updateItem(item, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(header) int userId, @PathVariable int itemId) {
        return itemService.getItemByIdAndUserId(userId, itemId);
    }

    @GetMapping
    public Collection<ItemDto> getAllUserItems(@RequestHeader(header) int userId) {
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestHeader(header) int userId, @RequestParam String text) {
        return itemService.searchItem(text, userId);
    }
}
