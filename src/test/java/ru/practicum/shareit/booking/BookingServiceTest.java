package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;


import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

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
        ItemDto itemDto = new ItemDto(null, "Отвёртка", "Отвёртка электрическая", Boolean.valueOf("true"), null);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.of(2024, 6, 30, 9, 0),
                LocalDateTime.of(2024, 6, 30, 10, 0));
        Booking bookingDtoForOut = bookingService.createBooking(savedBooker.getId(), bookingDtoForIn);
        assertThat(BookingStatus.WAITING, equalTo(bookingDtoForOut.getStatus()));
        //Assertions.assertEquals(BookingStatus.WAITING, bookingDtoForOut.getStatus(), "Статус нового бронирования не WAITING");
        Booking bookingDtoForOutAPPROVED = bookingService.approveBooking(savedOwner.getId(), bookingDtoForOut.getId(), "true");
        assertThat(BookingStatus.APPROVED, equalTo(bookingDtoForOutAPPROVED.getStatus()));
        //Assertions.assertEquals(BookingStatus.APPROVED, bookingDtoForOutAPPROVED.getStatus(), "Статус подтверждённого бронирования не APPROVED");
    }

    @Test
    void getBooking() {

        User userDto1 = new User(null, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(null, "Vladimir", "vladimir@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        ItemDto itemDto = new ItemDto(null, "Отвёртка", "Отвёртка электрическая", Boolean.valueOf("true"), 0);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        BookingInputDto bookingDtoForIn = new BookingInputDto(savedItem.getId(), LocalDateTime.of(2024, 6, 30, 9, 0),
                LocalDateTime.of(2024, 6, 30, 10, 0));
        Booking bookingDtoForOut = bookingService.createBooking(savedBooker.getId(), bookingDtoForIn);
        Booking receivedBooking = bookingService.getBookingById(savedOwner.getId(), bookingDtoForOut.getId());
        assertThat(receivedBooking.getId(), equalTo(bookingDtoForOut.getId()));
        //Assertions.assertEquals(receivedBooking.getId(), bookingDtoForOut.getId(), "Сохранённое и подтверждённое бронирования не совпадают");
    }
}
