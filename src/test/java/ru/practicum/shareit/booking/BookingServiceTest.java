package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
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
        BookingInputDto bookingDtoForIn = new BookingInputDto(1, LocalDateTime.of(2024, 6, 30, 9, 0),
                LocalDateTime.of(2024, 6, 30, 10, 0));
        User userDto1 = new User(0, "Andrey", "andrey@mail.ru");
        User savedOwner = userService.createUser(userDto1);
        User userDto2 = new User(0, "Vladimir", "vladimir@mail.ru");
        User savedBooker = userService.createUser(userDto2);
        ItemDto itemDto = new ItemDto(0, "Отвёртка", "Отвёртка электрическая", Boolean.valueOf("true"), 0);
        ItemDto savedItem = itemService.createItem(itemDto, savedOwner.getId());
        Booking bookingDtoForOut = bookingService.createBooking(savedBooker.getId(), bookingDtoForIn);
        Assertions.assertEquals(1, bookingDtoForOut.getId(), "Id сохранённого бронирования не соответствует ожидаемому");
    }
}
