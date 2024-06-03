package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {
    public Item mapDtoToItem(ItemDto item, int itemId, int ownerId) {
        return new Item(itemId, item.getName(), item.getDescription(), item.getAvailable(), ownerId);
    }

    public Item mapDtoToItem(ItemDto item, int ownerId) {
        return new Item(item.getName(), item.getDescription(), item.getAvailable(), ownerId);
    }

    public ItemDto mapItemToDto(Item item) {
        return new ItemDto(item.getId(), item.getItemName(), item.getDescription(), item.getAvailable());
    }
}
