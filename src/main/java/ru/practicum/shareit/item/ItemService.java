package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.BadParameterException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserHasNoAccess;
import ru.practicum.shareit.exception.WrongOwnerException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingService;

    @Transactional
    public ItemDto createItem(ItemDto itemDto, int userId) {
        User user = userService.getUser(userId);
        Item item = itemMapper.mapDtoToItem(itemDto, user);
        return itemMapper.mapItemToDto(itemRepository.save(item));
    }

    @Transactional
    public ItemDto updateItem(ItemDto itemDto, int itemId, int userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь с айди " + itemId
                + " не найдена"));
        if (item.getUser().getId() != userId)
            throw new WrongOwnerException("Пользователь не владелец вещи");
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());
        if (itemDto.getName() != null && !itemDto.getName().isBlank())
            item.setName(itemDto.getName());
        if (itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());
        return itemMapper.mapItemToDto(itemRepository.save(item));
    }

    @Transactional(readOnly = true)
    public ItemDtoWithBookingAndComment getItemByIdAndUserId(int userId, int itemId) {
        User user = userService.getUser(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь с айди " + itemId
                + " не найдена"));
        List<CommentDto> comments = commentRepository.findAllByItemId(itemId).stream()
                .map(itemMapper::mapCommentToDto)
                .collect(Collectors.toList());
        if (item.getUser().getId() == user.getId())
            return itemMapper.mapItemToDtoWithBookingAndComment(item, true, comments);
        else return itemMapper.mapItemToDtoWithBookingAndComment(item, false, comments);
        // return itemMapper.mapItemToDto(itemRepository.findByIdAndUserId(itemId,userId));
    }

    @Transactional(readOnly = true)
    public List<ItemDtoWithBooking> getAllUserItems(int userId) {
        return itemRepository.getAllUserItems(userId).stream().map(item -> itemMapper.mapItemToDtoWithBooking(item,
                        true))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemDto> searchItem(String text, int userId) {
        if (text.isBlank())
            return new ArrayList<>();
        return itemRepository.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(text,
                text).stream().map(itemMapper::mapItemToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Item getFullItemById(int itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь с айди " + itemId
                + " не найдена"));
    }

    @Transactional
    public CommentDto postComment(CommentDto comment, int userId, int itemId) {
        User user = userService.getUser(userId);
        Item item = getFullItemById(itemId);
        if (comment.getText().isBlank())
            throw new BadParameterException("Текст отзыва не может быть пустым");
        List<Booking> bookings = bookingService.findFirst1ByItemIdAndBookerIdAndEndIsBefore(itemId,
                userId, LocalDateTime.now());
        if (bookings.isEmpty())
            throw new UserHasNoAccess("Нельзя оставить отзыв без использования");
        return itemMapper.mapCommentToDto(commentRepository.save(itemMapper.mapDtoToComment(comment, user, item)));
    }
}
