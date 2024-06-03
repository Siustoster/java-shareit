package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.WrongOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;
    private final ItemMapper itemMapper;

    public ItemDto createItem(ItemDto itemDto, int userId) {
        userService.getUser(userId);
        Item item = itemMapper.mapDtoToItem(itemDto, userId);
        return itemMapper.mapItemToDto(itemStorage.createItem(item, userId));
    }

    public ItemDto updateItem(ItemDto itemDto, int itemId, int userId) {
        userService.getUser(userId);
        Item item = itemStorage.getItem(itemId);
        if (item.getOwnerId() != userId)
            throw new WrongOwnerException("Пользователь не владелец вещи");
        itemDto.setId(itemId);
        Item itemToUpdate = itemMapper.mapDtoToItem(itemDto, itemId, userId);
        return itemMapper.mapItemToDto(itemStorage.updateItem(itemToUpdate));
    }

    public ItemDto getItemByIdAndUserId(int userId, int itemId) {
        userService.getUser(userId);
        return itemMapper.mapItemToDto(itemStorage.getItem(itemId));
    }

    public List<ItemDto> getAllUserItems(int userId) {
        userService.getUser(userId);
        return itemStorage.getAllUserItems(userId).stream()
                .map(itemMapper::mapItemToDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> searchItem(String text, int userId) {
        userService.getUser(userId);
        if (text.isBlank())
            return new ArrayList<>();
        return itemStorage.searchItem(text).stream()
                .map(itemMapper::mapItemToDto)
                .collect(Collectors.toList());
    }
}
