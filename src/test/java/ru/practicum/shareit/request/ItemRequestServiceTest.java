package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutWithItems;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.transaction.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(properties = "db.name=test", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService requestService;

    ItemRequestDto itemRequestDto = new ItemRequestDto("description");
    User user = new User(null, "user", "user@mal.ru");
    User user2 = new User(null, "user2", "user2@mal.ru");

    @Test
    void createNewRequest() {
        int userId = userService.createUser(user).getId();
        ItemRequest itemRequest = requestService.createNewRequest(itemRequestDto, userId);
        assertThat(itemRequest.getId(), notNullValue());
        assertThat(itemRequest.getRequestor().getId(), equalTo(userId));
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
    }

    @Test
    void getAllUsersRequests() {
        int userId = userService.createUser(user).getId();
        ItemRequest itemRequest = requestService.createNewRequest(itemRequestDto, userId);
        List<ItemRequestDtoOutWithItems> itemRequests = requestService.getAllUsersRequests(userId);
        assertThat(itemRequests.size(), equalTo(1));
        assertThat(itemRequests.get(0).getId(), equalTo(itemRequest.getId()));
        assertThat(itemRequests.get(0).getRequestor().getId(), equalTo(userId));
    }

    @Test
    void getAllRequests() {
        int userId = userService.createUser(user).getId();
        ItemRequest itemRequest = requestService.createNewRequest(itemRequestDto, userId);
        List<ItemRequestDtoOutWithItems> itemRequests = requestService.getAllUsersRequests(userId);
        assertThat(itemRequests.size(), equalTo(1));
        assertThat(itemRequests.get(0).getId(), equalTo(itemRequest.getId()));
    }

    @Test
    void getRequestById() {
        int userId = userService.createUser(user).getId();
        int userId2 = userService.createUser(user2).getId();
        ItemRequest itemRequest = requestService.createNewRequest(itemRequestDto, userId);
        ItemRequestDtoOutWithItems gettedRequest = requestService.getRequestById(userId, itemRequest.getId());
        assertThat(gettedRequest.getId(), equalTo(itemRequest.getId()));
        assertThat(gettedRequest.getDescription(), equalTo(itemRequest.getDescription()));
        assertThat(gettedRequest.getRequestor().getId(), equalTo(userId));
    }

    @Test
    void getRequestByIdFailId() {
        int userId = userService.createUser(user).getId();
        int userId2 = userService.createUser(user2).getId();
        ItemRequest itemRequest = requestService.createNewRequest(itemRequestDto, userId);
        assertThrows(NotFoundException.class, () -> requestService.getRequestById(userId, 999));
    }

    @Test
    void getRequestsAndItems() {
        int userId = userService.createUser(user).getId();
        int userId2 = userService.createUser(user2).getId();
        ItemRequest createdRequest = requestService.createNewRequest(itemRequestDto, userId);
        ItemDto item = new ItemDto(null, "requestedItem", "itemdescr", true, createdRequest.getId());
        ItemDto createdItem = itemService.createItem(item, userId2);
        List<ItemRequestDtoOutWithItems> itemRequestWithItem = requestService.getRequestsAndItems(List.of(createdRequest));
        assertThat(itemRequestWithItem.size(), equalTo(1));
        assertThat(itemRequestWithItem.get(0).getId(), equalTo(createdRequest.getId()));
        assertThat(itemRequestWithItem.get(0).getItems().size(), equalTo(1));
        assertThat(itemRequestWithItem.get(0).getItems().get(0).getId(), equalTo(createdItem.getId()));
    }
}
