package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.exception.BadParameterException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedList;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTestWithContext {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService bookingService;
    @Autowired
    private MockMvc mvc;

    User user1 = new User(1, "user1", "user@mail.ru");
    Item item = new Item(1, "item1", "item1descr", true, user1);
    User user2 = new User(2, "user2", "user2@mail.ru");
    BookingInputDto bookingInputDto = new BookingInputDto(null, LocalDateTime.now(), LocalDateTime.now());
    Booking booking = new Booking(null, LocalDateTime.now(), item, user2, BookingStatus.WAITING);
    private static final String header = "X-Sharer-User-Id";

    @Test
    void createBookingRequest() throws Exception {
        booking.setStatus(BookingStatus.WAITING);
        when(bookingService.createBooking(anyInt(), any()))
                .thenReturn(booking);

        mvc.perform(post("/bookings")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId())))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId())))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId())));

    }

    @Test
    void approveBooking() throws Exception {
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingService.approveBooking(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(booking);

        mvc.perform(patch("/bookings/1")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId())))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId())))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId())));

    }

    @Test
    void getBookingById() throws Exception {
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingService.getBookingById(anyInt(), anyInt()))
                .thenReturn(booking);

        mvc.perform(get("/bookings/1")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId())))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId())))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId())));

    }

    @Test
    void getAllBookingsOfCurrentUser() throws Exception {
        booking.setStatus(BookingStatus.APPROVED);
        LinkedList<Booking> listOfBookings = new LinkedList<>();
        listOfBookings.add(booking);
        when(bookingService.getBookingByUser(anyInt(), anyString(), any()))
                .thenReturn(listOfBookings);

        mvc.perform(get("/bookings")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(booking.getId())))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus().toString())))
                .andExpect(jsonPath("$[0].item.id", is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].booker.id", is(booking.getBooker().getId())));
    }

    @Test
    void getAllBookingsForAllUserItems() throws Exception {
        booking.setStatus(BookingStatus.APPROVED);
        LinkedList<Booking> listOfBookings = new LinkedList<>();
        listOfBookings.add(booking);
        when(bookingService.getBookingByUserItems(anyInt(), anyString(), any()))
                .thenReturn(listOfBookings);

        mvc.perform(get("/bookings/owner")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(booking.getId())))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus().toString())))
                .andExpect(jsonPath("$[0].item.id", is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].booker.id", is(booking.getBooker().getId())));

    }

    @Test
    void getAllBookingsForAllUserItemsFail() throws Exception {
        booking.setStatus(BookingStatus.APPROVED);
        LinkedList<Booking> listOfBookings = new LinkedList<>();
        listOfBookings.add(booking);
        when(bookingService.getBookingByUserItems(anyInt(), anyString(), any()))
                .thenThrow(new BadParameterException("Неверные параметры страницы"));

        mvc.perform(get("/bookings/owner")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllBookingsForAllUserItemsFailPage() throws Exception {
        booking.setStatus(BookingStatus.APPROVED);
        LinkedList<Booking> listOfBookings = new LinkedList<>();
        listOfBookings.add(booking);
        when(bookingService.getBookingByUserItems(anyInt(), anyString(), any()))
                .thenThrow(new BadParameterException("Неверные параметры страницы"));

        mvc.perform(get("/bookings/owner?from=-5")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllBookingsOfCurrentUserFailPage() throws Exception {
        booking.setStatus(BookingStatus.APPROVED);
        LinkedList<Booking> listOfBookings = new LinkedList<>();
        listOfBookings.add(booking);
        when(bookingService.getBookingByUser(anyInt(), anyString(), any()))
                .thenReturn(listOfBookings);

        mvc.perform(get("/bookings?from=-5")
                        .header(header, 1)
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
}
