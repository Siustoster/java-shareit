package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @Test
    void createItem() {
        int userId = 1;
        ItemDto item = new ItemDto(null, "itemName", "descr", true, null);
        ItemDto createdItem = new ItemDto(1, "itemName", "descr", true, null);

        when(itemService.createItem(item, userId))
                .thenReturn(createdItem);
        ItemDto response = itemController.createItem(userId, item);

        assertEquals(response.getId(), 1);
        assertEquals(response.getName(), createdItem.getName());
        assertEquals(response.getDescription(), createdItem.getDescription());
        assertEquals(response.getAvailable(), createdItem.getAvailable());
    }

    @Test
    void updateItem() {
        int userId = 1;
        ItemDto item = new ItemDto(null, "itemName2", "descr", true, null);
        ItemDto updated = new ItemDto(1, "itemName2", "descr", true, null);

        when(itemService.updateItem(item, 1, 1))
                .thenReturn(updated);
        ItemDto response = itemController.updateItem(userId, 1, item);

        assertEquals(response.getId(), 1);
        assertEquals(response.getName(), updated.getName());
        assertEquals(response.getDescription(), updated.getDescription());
        assertEquals(response.getAvailable(), updated.getAvailable());
    }

    @Test
    void getItemById() {
        ItemDtoWithBookingAndComment item = new ItemDtoWithBookingAndComment(1, "name", "descr", true,
                null, null, null);
        when(itemService.getItemByIdAndUserId(1, 1))
                .thenReturn(item);
        ItemDtoWithBookingAndComment result = itemController.getItemById(1, 1);
        assertEquals(result.getId(), item.getId());
        assertEquals(result.getName(), item.getName());
        assertEquals(result.getDescription(), item.getDescription());
        assertEquals(result.getAvailable(), item.getAvailable());
    }

    @Test
    void searchItem() {
        ItemDto item = new ItemDto();
        List<ItemDto> collection = new ArrayList<>();
        collection.add(item);
        when(itemService.searchItem(any(), anyInt()))
                .thenReturn(collection);
        List<ItemDto> result = (List<ItemDto>) itemController.searchItem(1, "easd");

        assertEquals(result.size(), 1);
    }

    @Test
    void getAllUserItems() {
        ItemDtoWithBooking item = new ItemDtoWithBooking();
        List<ItemDtoWithBooking> collection = new ArrayList<>();
        collection.add(item);
        when(itemService.getAllUserItems(anyInt()))
                .thenReturn(collection);
        Collection<ItemDtoWithBooking> result = itemController.getAllUserItems(1);

        assertEquals(result.size(), 1);
    }
}
