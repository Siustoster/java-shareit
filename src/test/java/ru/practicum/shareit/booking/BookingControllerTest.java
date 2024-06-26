package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.exception.BadParameterException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    @Mock
    BookingService bookingService;

    @InjectMocks
    BookingController bookingController;

    @Test
    void createBooking() {
        User user = new User(1, "user", "user@mail.ru");
        Item item = new Item(1, "name", "description", true, user);
        Booking resultBooking = new Booking(1, LocalDateTime.now(), LocalDateTime.now(), item, user,
                BookingStatus.WAITING);
        BookingInputDto inputDto = new BookingInputDto(1, LocalDateTime.now(), LocalDateTime.now());
        when(bookingService.createBooking(user.getId(), inputDto))
                .thenReturn(resultBooking);

        Booking response = bookingController.createBookingRequest(user.getId(), inputDto);

        assertEquals(response.getId(), resultBooking.getId());
        assertEquals(response.getBooker().getId(), resultBooking.getBooker().getId());
        assertEquals(response.getItem().getId(), resultBooking.getItem().getId());
    }

    @Test
    void getAllBookingsOfCurrentUser() {
        LinkedList<Booking> bookingList = new LinkedList<>();
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now(), new Item(), new User(), BookingStatus.APPROVED);
        bookingList.add(booking);
        when(bookingService.getBookingByUser(eq(1), eq("ALL"), any()))
                .thenReturn(bookingList);
        List<Booking> result = bookingController.getAllBookingsOfCurrentUser(1, "ALL", 1, 5);
        assertEquals(result.size(), 1);
    }

    @Test
    void getAllBookingsForAllUserItems() {
        LinkedList<Booking> bookingList = new LinkedList<>();
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now(), new Item(), new User(), BookingStatus.APPROVED);
        bookingList.add(booking);
        when(bookingService.getBookingByUserItems(eq(1), eq("ALL"), any()))
                .thenReturn(bookingList);
        List<Booking> result = bookingController.getAllBookingsForAllUserItems(1, "ALL", 1, 5);
        assertEquals(result.size(), 1);
    }

    @Test
    void getAllBookingsOfCurrentUserFailPage() {
        assertThrows(BadParameterException.class, () -> bookingController.getAllBookingsOfCurrentUser(1, "ALL", -5, 6));
        // bookingController.getAllBookingsOfCurrentUser()
    }

    @Test
    void getAllBookingsForAllUserItemsFailPage() {
        assertThrows(BadParameterException.class, () -> bookingController.getAllBookingsForAllUserItems(1, "ALL", -5, 6));
    }

    @Test
    void getAllBookingsOfCurrentUserFailPage2() {
        assertThrows(BadParameterException.class, () -> bookingController.getAllBookingsOfCurrentUser(1, "ALL", 1, 0));
        // bookingController.getAllBookingsOfCurrentUser()
    }

    @Test
    void getAllBookingsForAllUserItemsFailPage2() {
        assertThrows(BadParameterException.class, () -> bookingController.getAllBookingsForAllUserItems(1, "ALL", 1, 0));
    }

    @Test
    void approveBooking() {
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now(), new Item(), new User(), BookingStatus.APPROVED);
        when(bookingService.approveBooking(eq(1), eq(1), anyBoolean()))
                .thenReturn(booking);
        Booking result = bookingController.approveBooking(1, 1, "true");
        assertEquals(result.getId(), booking.getId());
        assertEquals(result.getStatus(), booking.getStatus());
    }

    @Test
    void getBookingById() {
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now(), new Item(), new User(), BookingStatus.APPROVED);
        when(bookingService.getBookingById(anyInt(), anyInt()))
                .thenReturn(booking);
        Booking result = bookingController.getBookingById(1, 1);
        assertEquals(result.getId(), booking.getId());
    }
}
