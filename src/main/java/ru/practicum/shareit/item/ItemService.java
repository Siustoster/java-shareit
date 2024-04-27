package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.WrongOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    public ItemDto createItem(ItemDto itemDto, int userId) {
        userService.getUser(userId);
        Item item = mapDtoToItem(itemDto, userId);
        return mapItemToDto(itemStorage.createItem(item, userId));
    }

    public ItemDto updateItem(ItemDto itemDto, int itemId, int userId) {
        userService.getUser(userId);
        Item item = getItem(itemId);
        if (item.getOwnerId() != userId)
            throw new WrongOwnerException("Пользователь не владелец вещи");
        itemDto.setId(itemId);
        Item itemToUpdate = mapDtoToItem(itemDto, itemId, userId);
        return mapItemToDto(itemStorage.updateItem(itemToUpdate));
    }

    public ItemDto getItemById(int userId, int itemId) {
        userService.getUser(userId);
        return mapItemToDto(itemStorage.getItem(itemId));
    }

    public List<ItemDto> getAllUserItems(int userId) {
        userService.getUser(userId);
        List<Item> itemsFounded = itemStorage.getAllUserItems(userId);
        List<ItemDto> itemsDto = new ArrayList<>();

        for (Item item : itemsFounded)
            itemsDto.add(mapItemToDto(item));
        return itemsDto;
    }

    public List<ItemDto> searchItem(String text, int userId) {
        userService.getUser(userId);
        List<ItemDto> itemsDto = new ArrayList<>();
        if (text.isBlank())
            return itemsDto;
        List<Item> itemsFounded = itemStorage.searchItem(text);
        for (Item item : itemsFounded)
            itemsDto.add(mapItemToDto(item));
        return itemsDto;
    }

    private Item mapDtoToItem(ItemDto item, int itemId, int ownerId) {
        return new Item(itemId, item.getName(), item.getDescription(), item.getAvailable(), ownerId);
    }

    private Item mapDtoToItem(ItemDto item, int ownerId) {
        return new Item(item.getName(), item.getDescription(), item.getAvailable(), ownerId);
    }

    private ItemDto mapItemToDto(Item item) {
        return new ItemDto(item.getId(), item.getItemName(), item.getDescription(), item.getAvailable());
    }

    private Item getItem(int id) {
        return itemStorage.getItem(id);
    }
}
