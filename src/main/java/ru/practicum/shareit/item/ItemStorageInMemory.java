package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemStorageInMemory implements ItemStorage {
    private int idCount = 0;
    private LinkedHashMap<Integer, Item> itemList = new LinkedHashMap<>();

    @Override
    public Item createItem(Item item, int userId) {
        idCount++;
        item.setId(idCount);
        itemList.put(item.getId(), item);
        return itemList.get(item.getId());
    }

    @Override
    public Item updateItem(Item item) {
        if (item.getItemName() != null)
            itemList.get(item.getId()).setItemName(item.getItemName());
        if (item.getDescription() != null)
            itemList.get(item.getId()).setDescription(item.getDescription());
        if (item.getAvailable() != null)
            itemList.get(item.getId()).setAvailable(item.getAvailable());
        return itemList.get(item.getId());
    }

    @Override
    public Item getItem(int itemId) {
        if (itemList.get(itemId) == null)
            throw new NotFoundException("Вещь с айди " + itemId + " не найдена");
        return itemList.get(itemId);
    }

    @Override
    public List<Item> getAllUserItems(int userId) {
        return itemList.values().stream().filter(item ->
                item.getOwnerId() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItem(String text) {
        return itemList.values().stream()
                .filter(item -> item.getItemName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }
}
