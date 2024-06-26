package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingInputDto;
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

import static java.lang.Thread.sleep;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    User user = new User(null, "user", "user@mail.ru");
    ItemDto item = new ItemDto(null, "item", "itemdescr", true, null);
    CommentDto comment = new CommentDto(null, "text", null, null);

    @Test
    void updateItem() {
        int userId = userService.createUser(user).getId();
        ItemDto createdItem = itemService.createItem(item, userId);
        createdItem.setName("updated");
        ItemDto updatedItem = itemService.updateItem(createdItem, createdItem.getId(), userId);
        assertThat(updatedItem.getId(), equalTo(createdItem.getId()));
        assertThat(updatedItem.getName(), equalTo(createdItem.getName()));
    }

    @Test
    void createItemFailByRequestId() {
        int userId = userService.createUser(user).getId();
        ItemDto item2 = new ItemDto(null, "item2", "item2Descr", true, 999);
        assertThrows(NotFoundException.class, () -> itemService.createItem(item2, userId));

    }

    @Test
    void updateItemFailByWrongOwner() {
        int userId = userService.createUser(user).getId();
        ItemDto createdItem = itemService.createItem(item, userId);
        createdItem.setName("updated");
        //ItemDto updatedItem = itemService.updateItem(createdItem, createdItem.getId(), 999);
        assertThrows(WrongOwnerException.class, () -> itemService.updateItem(createdItem, createdItem.getId(), 999));

    }

    @Test
    void getItemByIdAndUserId() {
        int userId = userService.createUser(user).getId();
        ItemDto createdItem = itemService.createItem(item, userId);
        ItemDtoWithBookingAndComment itemDto = itemService.getItemByIdAndUserId(userId, createdItem.getId());
        assertThat(itemDto.getId(), equalTo(createdItem.getId()));
        assertThat(itemDto.getComments(), empty());
        assertThat(itemDto.getLastBooking(), nullValue());
        assertThat(itemDto.getNextBooking(), nullValue());
    }

    @Test
    void postComment() throws InterruptedException {
        User userDto1 = new User(0, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(0, "Vladimir", "vladimir@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        ItemDto itemDto = new ItemDto(0, "Отвёртка", "Отвёртка электрическая",
                Boolean.valueOf("true"), null);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2));
        bookingService.createBooking(savedBooker.getId(), bookingDtoForIn);
        sleep(3000);
        //int userId = userService.createUser(user).getId();
        //ItemDto createdItem = itemService.createItem(item, userId);
        CommentDto createdComment = itemService.postComment(comment, savedBooker.getId(), savedItem.getId());
        assertThat(createdComment.getId(), notNullValue());
        assertThat(createdComment.getText(), equalTo(comment.getText()));
        assertThat(createdComment.getAuthorName(), equalTo(userDto2.getName()));
    }

    @Test
    void getAllUserItems() {
        int userId = userService.createUser(user).getId();
        ItemDto createdItem = itemService.createItem(item, userId);
        List<ItemDtoWithBooking> itemList = itemService.getAllUserItems(userId);
        assertThat(itemList.size(), equalTo(1));
    }

    @Test
    void searchItem() {
        int userId = userService.createUser(user).getId();
        ItemDto createdItem = itemService.createItem(item, userId);
        List<ItemDto> searchItemList = itemService.searchItem("itemdescr", userId);
        assertThat(searchItemList.size(), equalTo(1));
    }

    @Test
    void getFullItemById() {
        int userId = userService.createUser(user).getId();
        ItemDto createdItem = itemService.createItem(item, userId);
        Item fullItem = itemService.getFullItemById(createdItem.getId());
        assertThat(createdItem.getId(), equalTo(fullItem.getId()));
    }

    @Test
    void postCommentFailByEmptyText() throws InterruptedException {
        User userDto1 = new User(0, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(0, "Vladimir", "vladimir@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        ItemDto itemDto = new ItemDto(0, "Отвёртка", "Отвёртка электрическая",
                Boolean.valueOf("true"), null);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2));
        bookingService.createBooking(savedBooker.getId(), bookingDtoForIn);
        sleep(3000);
        CommentDto failedComment = new CommentDto(null, "", null, null);
        assertThrows(BadParameterException.class, () -> itemService.postComment(failedComment, savedBooker.getId(), savedItem.getId()));
    }

    @Test
    void postCommentFailByNotBookedBefore() throws InterruptedException {
        User userDto1 = new User(0, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(0, "Vladimir", "vladimir@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        ItemDto itemDto = new ItemDto(0, "Отвёртка", "Отвёртка электрическая",
                Boolean.valueOf("true"), null);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2));
        assertThrows(UserHasNoAccess.class, () -> itemService.postComment(comment, savedBooker.getId(), savedItem.getId()));
    }

    @Test
    void getItemByIdAndUserIdAnotherUser() {
        int userId = userService.createUser(user).getId();
        int userId2 = userService.createUser(new User(null, "Name", "email@mail.ru")).getId();
        ItemDto createdItem = itemService.createItem(item, userId);
        ItemDtoWithBookingAndComment itemDto = itemService.getItemByIdAndUserId(userId2, createdItem.getId());
        assertThat(itemDto.getId(), equalTo(createdItem.getId()));
        assertThat(itemDto.getComments(), empty());
        assertThat(itemDto.getLastBooking(), nullValue());
        assertThat(itemDto.getNextBooking(), nullValue());
    }

    @Test
    void searchItemEmptyString() {
        int userId = userService.createUser(user).getId();
        ItemDto createdItem = itemService.createItem(item, userId);
        List<ItemDto> searchItemList = itemService.searchItem("", userId);
        assertThat(searchItemList.size(), equalTo(0));
    }
}
