package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.exception.BadDateException;
import ru.practicum.shareit.exception.ItemIsUnavailableExeption;
import ru.practicum.shareit.exception.UserHasNoAccess;
import ru.practicum.shareit.exception.WrongOwnerException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;


import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void add() {

        User userDto1 = new User(0, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(0, "Vladimir", "vladimir@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        ItemDto itemDto = new ItemDto(0, "Отвёртка", "Отвёртка электрическая",
                Boolean.valueOf("true"), null);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.of(2024,
                6, 30, 9, 0),
                LocalDateTime.of(2024, 6, 30, 10, 0));
        Booking bookingDtoForOut = bookingService.createBooking(savedBooker.getId(), bookingDtoForIn);
        assertThat(bookingDtoForOut.getId(), notNullValue());
    }

    @Test
    void confirm() {

        User userDto1 = new User(null, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(null, "Vladimir", "vladimir@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        ItemDto itemDto = new ItemDto(null, "Отвёртка", "Отвёртка электрическая",
                Boolean.valueOf("true"), null);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.of(2024,
                6, 30, 9, 0),
                LocalDateTime.of(2024, 6, 30, 10, 0));
        Booking bookingDtoForOut = bookingService.createBooking(savedBooker.getId(), bookingDtoForIn);
        assertThat(BookingStatus.WAITING, equalTo(bookingDtoForOut.getStatus()));
        Booking bookingDtoForOutAPPROVED = bookingService.approveBooking(savedOwner.getId(),
                bookingDtoForOut.getId(), true);
        assertThat(BookingStatus.APPROVED, equalTo(bookingDtoForOutAPPROVED.getStatus()));

    }

    @Test
    void getBooking() {

        User userDto1 = new User(null, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(null, "Vladimir", "vladimir@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        ItemDto itemDto = new ItemDto(null, "Отвёртка", "Отвёртка электрическая",
                Boolean.valueOf("true"), 0);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.of(2024,
                6, 30, 9, 0),
                LocalDateTime.of(2024, 6, 30, 10, 0));
        Booking bookingDtoForOut = bookingService.createBooking(savedBooker.getId(), bookingDtoForIn);
        Booking receivedBooking = bookingService.getBookingById(savedOwner.getId(), bookingDtoForOut.getId());
        assertThat(receivedBooking.getId(), equalTo(bookingDtoForOut.getId()));
    }

    @Test
    void getBookingFailWrongOwner() {
        User userDto1 = new User(null, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(null, "Vladimir", "vladimir@mail.ru");
        User userDto3 = new User(null, "user", "user@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        int wrongUserId = userService.createUser(userDto3).getId();
        ItemDto itemDto = new ItemDto(null, "Отвёртка", "Отвёртка электрическая",
                Boolean.valueOf("true"), 0);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.of(2024,
                6, 30, 9, 0),
                LocalDateTime.of(2024, 6, 30, 10, 0));
        Booking bookingDtoForOut = bookingService.createBooking(savedBooker.getId(), bookingDtoForIn);
        assertThrows(WrongOwnerException.class, () -> bookingService.getBookingById(wrongUserId,
                bookingDtoForOut.getId()));
    }

    @Test
    void approveBookingFailUser() {
        User userDto1 = new User(null, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(null, "Vladimir", "vladimir@mail.ru");
        User userDto3 = new User(null, "user", "user@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        int wrongUserId = userService.createUser(userDto3).getId();
        ItemDto itemDto = new ItemDto(null, "Отвёртка", "Отвёртка электрическая",
                Boolean.valueOf("true"), null);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.of(2024, 6,
                30, 9, 0), LocalDateTime.of(2024, 6, 30, 10, 0));
        Booking bookingDtoForOut = bookingService.createBooking(savedBooker.getId(), bookingDtoForIn);
        assertThat(BookingStatus.WAITING, equalTo(bookingDtoForOut.getStatus()));
        assertThrows(UserHasNoAccess.class, () -> bookingService.approveBooking(wrongUserId, bookingDtoForOut.getId(),
                true));
    }

    @Test
    void getBookingByUser() {
        User userDto1 = new User(null, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(null, "Vladimir", "vladimir@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        ItemDto itemDto = new ItemDto(null, "Отвёртка", "Отвёртка электрическая",
                Boolean.valueOf("true"), null);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.of(2024, 6,
                30, 9, 0), LocalDateTime.of(2024, 6, 30, 10, 0));
        Booking bookingDtoForOut = bookingService.createBooking(savedBooker.getId(), bookingDtoForIn);
        List<Booking> bookingList = bookingService.getBookingByUser(savedBooker.getId(), "ALL",
                PageRequest.of(0, 5));
        assertThat(bookingList.size(), equalTo(1));
    }

    @Test
    void getBookingByUserItems() {
        User userDto1 = new User(null, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(null, "Vladimir", "vladimir@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        ItemDto itemDto = new ItemDto(null, "Отвёртка", "Отвёртка электрическая",
                Boolean.valueOf("true"), null);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.of(2024, 6,
                30, 9, 0), LocalDateTime.of(2024, 6, 30, 10, 0));
        Booking bookingDtoForOut = bookingService.createBooking(savedBooker.getId(), bookingDtoForIn);
        List<Booking> bookingList = bookingService.getBookingByUserItems(savedOwner.getId(), "ALL",
                PageRequest.of(0, 5));
        assertThat(bookingList.size(), equalTo(1));
    }

    @Test
    void addFailByStartDate() {

        User userDto1 = new User(0, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(0, "Vladimir", "vladimir@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        ItemDto itemDto = new ItemDto(0, "Отвёртка", "Отвёртка электрическая",
                Boolean.valueOf("true"), null);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.of(2024,
                6, 15, 9, 0),
                LocalDateTime.of(2024, 6, 30, 10, 0));
        // bookingService.createBooking(savedBooker.getId(), bookingDtoForIn);
        assertThrows(BadDateException.class, () -> bookingService.createBooking(savedBooker.getId(), bookingDtoForIn));
    }

    @Test
    void addFailByEndDate() {

        User userDto1 = new User(0, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(0, "Vladimir", "vladimir@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        ItemDto itemDto = new ItemDto(0, "Отвёртка", "Отвёртка электрическая",
                Boolean.valueOf("true"), null);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.of(2024,
                6, 30, 9, 0),
                LocalDateTime.of(2024, 6, 15, 10, 0));
        // bookingService.createBooking(savedBooker.getId(), bookingDtoForIn);
        assertThrows(BadDateException.class, () -> bookingService.createBooking(savedBooker.getId(), bookingDtoForIn));
    }

    @Test
    void addFailByUnavailable() {
        User userDto1 = new User(0, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(0, "Vladimir", "vladimir@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        ItemDto itemDto = new ItemDto(0, "Отвёртка", "Отвёртка электрическая",
                false, null);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.of(2024,
                6, 30, 9, 0),
                LocalDateTime.of(2024, 6, 30, 10, 0));
        assertThrows(ItemIsUnavailableExeption.class, () -> bookingService.createBooking(savedBooker.getId(), bookingDtoForIn));
    }

}
