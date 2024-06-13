package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComment;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper = new BookingMapper();

    public Item mapDtoToItem(ItemDto item, int itemId, User user) {
        return new Item(itemId, item.getName(), item.getDescription(), item.getAvailable(), user);
    }

    public Item mapDtoToItem(ItemDto item, User user) {
        return new Item(item.getName(), item.getDescription(), item.getAvailable(), user);
    }

    public ItemDto mapItemToDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }


    public ItemDtoWithBooking mapItemToDtoWithBooking(Item item, Boolean getBookings) {
        LocalDateTime now = LocalDateTime.now();
        ItemDtoWithBooking resultItem = new ItemDtoWithBooking();
        resultItem.setId(item.getId());
        resultItem.setDescription(item.getDescription());
        resultItem.setName(item.getName());
        resultItem.setAvailable(item.getAvailable());

        if (getBookings) {
            List<BookingDto> lastBookingDto = bookingRepository
                    .findFirst1ByItemIdAndStartIsBeforeAndStatusOrderByStartDesc(item.getId(),
                            now, BookingStatus.APPROVED);
            List<BookingDto> nextBookingDto = bookingRepository
                    .findFirst1ByItemIdAndStartIsAfterAndStatusOrderByStartAsc(item.getId(),
                            now, BookingStatus.APPROVED);

            if (!lastBookingDto.isEmpty()) {
                resultItem.setLastBooking(bookingMapper.mapBookingDtoToOutInformation(lastBookingDto.get(0)));
            }
            if (!nextBookingDto.isEmpty())
                resultItem.setNextBooking(bookingMapper.mapBookingDtoToOutInformation(nextBookingDto.get(0)));
        }
        return resultItem;
    }

    public ItemDtoWithBookingAndComment mapItemToDtoWithBookingAndComment(Item item, Boolean getBookings,
                                                                          List<CommentDto> comments) {
        ItemDtoWithBooking proxyItem = mapItemToDtoWithBooking(item, getBookings);

        return new ItemDtoWithBookingAndComment(proxyItem.getId(), proxyItem.getName(), proxyItem.getDescription(),
                proxyItem.getAvailable(), proxyItem.getLastBooking(),
                proxyItem.getNextBooking(), comments);
    }

    public Comment mapDtoToComment(CommentDto commentDto, User user, Item item) {
        return new Comment(commentDto.getText(), user, item, LocalDateTime.now());
    }

    public CommentDto mapCommentToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getUser().getName(), comment.getCreated());
    }
}
