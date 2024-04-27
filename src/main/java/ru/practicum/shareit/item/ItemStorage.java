package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item createItem(Item item, int userId);

    Item updateItem(Item item);

    Item getItem(int itemId);

    List<Item> getAllUserItems(int userId);

    List<Item> searchItem(String text);
}
