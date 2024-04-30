package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ItemService itemService;
    private final String HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createItem(@RequestHeader(HEADER) int userId, @RequestBody @Valid ItemDto item) {
        return itemService.createItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(HEADER) int userId, @PathVariable int itemId,
                              @RequestBody ItemDto item) {
        return itemService.updateItem(item, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(HEADER) int userId, @PathVariable int itemId) {
        return itemService.getItemByIdAndUserId(userId, itemId);
    }

    @GetMapping
    public Collection<ItemDto> getAllUserItems(@RequestHeader(HEADER) int userId) {
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestHeader(HEADER) int userId, @RequestParam String text) {
        return itemService.searchItem(text, userId);
    }
}
